import org.junit.jupiter.api.*;
import org.os.Main;
import org.os.commands.MkdirCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class MkdirCommandTest {

    private final MkdirCommand mkdirCommand = new MkdirCommand();
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final Path testDir = Paths.get("testMkdir");

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outputStream));
        Files.createDirectories(testDir);
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(System.out);
        Files.walk(testDir)
                .map(Path::toFile)
                .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                .forEach(f -> f.delete());
    }

    @Test
    void testMkdirWithoutArguments() {
        mkdirCommand.execute(new String[]{});
        String output = outputStream.toString().trim();
        assertEquals("Usage: mkdir <directory-name>", output);
    }

    @Test
    void testMkdirWhenParentDirectoryDoesNotExist() {
        Main.currentDirectory = "nonExistentParentDir/subDir";
        mkdirCommand.execute(new String[]{"subDir"});
        String output = outputStream.toString().trim();
        assertTrue(output.contains("Parent directory does not exist:"));
    }

    @Test
    void testMkdirWhenFileWithSameNameExists() throws IOException {
        Files.createFile(testDir.resolve("existingFile.txt"));
        Main.currentDirectory = testDir.resolve("existingFile.txt").toString();
        mkdirCommand.execute(new String[]{"existingFile.txt"});
        String output = outputStream.toString().trim();
        assertTrue(output.contains("A file with the same name exists: existingFile.txt"));
    }

    @Test
    void testMkdirCreatesDirectorySuccessfully() {
        Main.currentDirectory = testDir.resolve("newDir").toString();
        mkdirCommand.execute(new String[]{"newDir"});
        String output = outputStream.toString().trim();
        assertTrue(output.contains("Directory created:"));
        assertTrue(Files.exists(testDir.resolve("newDir")));
    }

    @Test
    void testMkdirThrowsIOException() throws IOException {
        Path restrictedDir = testDir.resolve("restrictedDir");
        Files.createDirectories(restrictedDir);
        restrictedDir.toFile().setReadOnly();  // Make directory read-only

        Main.currentDirectory = restrictedDir.toString();
        mkdirCommand.execute(new String[]{"newDirInRestricted"});
        String output = outputStream.toString().trim();

        assertTrue(output.contains("Error creating directory:"));
    }
}
