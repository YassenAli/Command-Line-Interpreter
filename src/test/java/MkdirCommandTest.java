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
        Main.currentDirectory = testDir.toString();
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
        assertEquals("Usage: mkdir <directory-name(s)>", output);
    }

    @Test
    void testMkdirCreatesMultipleDirectoriesSuccessfully() {
        mkdirCommand.execute(new String[]{"dir1", "dir2", "dir3"});
        String output = outputStream.toString().trim();

        assertTrue(output.contains("Directory created: " + testDir.resolve("dir1")));
        assertTrue(output.contains("Directory created: " + testDir.resolve("dir2")));
        assertTrue(output.contains("Directory created: " + testDir.resolve("dir3")));

        assertTrue(Files.exists(testDir.resolve("dir1")));
        assertTrue(Files.exists(testDir.resolve("dir2")));
        assertTrue(Files.exists(testDir.resolve("dir3")));
    }

    @Test
    void testMkdirWhenParentDirectoryDoesNotExist() {
        Main.currentDirectory = "nonExistentParentDir/subDir";
        mkdirCommand.execute(new String[]{"dir1", "dir2"});
        String output = outputStream.toString().trim();

        assertTrue(output.contains("Parent directory does not exist:"));
        assertFalse(Files.exists(testDir.resolve("dir1")));
        assertFalse(Files.exists(testDir.resolve("dir2")));
    }

    @Test
    void testMkdirWhenFileWithSameNameExists() throws IOException {
        Files.createFile(testDir.resolve("existingFile.txt"));
        Main.currentDirectory = testDir.toString();

        mkdirCommand.execute(new String[]{"existingFile.txt", "newDir"});
        String output = outputStream.toString().trim();

        assertTrue(output.contains("A file with the same name exists: existingFile.txt"));
        assertTrue(Files.exists(testDir.resolve("existingFile.txt")));
        assertTrue(output.contains("Directory created: " + testDir.resolve("newDir")));
        assertTrue(Files.exists(testDir.resolve("newDir")));
    }

    @Test
    void testMkdirCreatesOneDirectorySuccessfullyWhenOthersFail() throws IOException {
        Files.createFile(testDir.resolve("existingFile.txt"));
        Main.currentDirectory = testDir.toString();

        mkdirCommand.execute(new String[]{"newDir1", "existingFile.txt", "newDir2"});
        String output = outputStream.toString().trim();

        assertTrue(output.contains("Directory created: " + testDir.resolve("newDir1")));
        assertTrue(output.contains("A file with the same name exists: existingFile.txt"));
        assertTrue(output.contains("Directory created: " + testDir.resolve("newDir2")));

        assertTrue(Files.exists(testDir.resolve("newDir1")));
        assertTrue(Files.exists(testDir.resolve("newDir2")));
        assertTrue(Files.exists(testDir.resolve("existingFile.txt")));
    }
}
