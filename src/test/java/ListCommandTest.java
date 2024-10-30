import org.junit.jupiter.api.*;
import org.os.Main;
import org.os.commands.ListCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class ListCommandTest {

    private final ListCommand listCommand = new ListCommand();
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final Path testDir = Paths.get("testDir");

    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outputStream));
        Files.createDirectories(testDir);
        Files.createFile(testDir.resolve("file1.txt"));
        Files.createFile(testDir.resolve(".hiddenFile.txt"));
        Files.createDirectories(testDir.resolve("subDir"));
    }

    @AfterEach
    void tearDown() throws Exception {
        System.setOut(System.out);
        Files.walk(testDir)
                .map(Path::toFile)
                .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                .forEach(f -> f.delete());
    }

    @Test
    void testListFilesNonRecursive() {
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{});
        String output = outputStream.toString();

        assertTrue(output.contains("- file1.txt"));
        assertTrue(output.contains("d subDir"));
        assertFalse(output.contains(".hiddenFile.txt"));
    }

    @Test
    void testListFilesWithHiddenFlag() {
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{"-a"});
        String output = outputStream.toString();

        assertTrue(output.contains("- file1.txt"));
        assertTrue(output.contains("d subDir"));
        assertTrue(output.contains("- .hiddenFile.txt"));
    }

    @Test
    void testListFilesInvalidDirectory() {
        Main.currentDirectory = "nonExistentDir";
        listCommand.execute(new String[]{});
        String output = outputStream.toString().trim();

        assertEquals("Invalid directory: .", output);
    }

    @Test
    void testListFilesRecursive() throws IOException {
        Files.createFile(testDir.resolve("subDir").resolve("fileInSubDir.txt"));
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{"-r"});
        String output = outputStream.toString();

        assertTrue(output.contains("- file1.txt"));
        assertTrue(output.contains("d subDir"));
        assertTrue(output.contains("  - fileInSubDir.txt"));
        assertFalse(output.contains(".hiddenFile.txt"));
    }

    @Test
    void testListFilesRecursiveWithHidden() throws IOException, IOException {
        Files.createFile(testDir.resolve("subDir").resolve(".hiddenFileInSubDir.txt"));
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{"-a", "-r"});
        String output = outputStream.toString();

        assertTrue(output.contains("- file1.txt"));
        assertTrue(output.contains("d subDir"));
        assertTrue(output.contains("  - .hiddenFileInSubDir.txt"));
        assertTrue(output.contains("- .hiddenFile.txt"));
    }
}
