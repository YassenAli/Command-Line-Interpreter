import org.junit.jupiter.api.*;
import org.os.commands.ListCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ListCommandTest {
    private ListCommand listCommand;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Path testDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        listCommand = new ListCommand();
        testDirectory = Files.createTempDirectory("testDir");
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up the test directory and its contents
        if (Files.exists(testDirectory)) {
            deleteDirectory(testDirectory);
        }
        System.setOut(originalOut);
    }

    private void deleteDirectory(Path directory) throws IOException {
        if (Files.isDirectory(directory)) {
            // List and delete all files and subdirectories
            Files.list(directory).forEach(path -> {
                try {
                    deleteDirectory(path); // Recursively delete subdirectories
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        Files.delete(directory); // Delete the directory or file
    }

    @Test
    public void testExecuteListsFiles() throws IOException {
        // Create a test file
        Path testFile = testDirectory.resolve("testFile.txt");
        Files.createFile(testFile);

        listCommand.execute(new String[]{testDirectory.toString()});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("- testFile.txt"), "Output should list the created file.");
    }

    @Test
    public void testExecuteWithHiddenFile() throws IOException {
        // Create a hidden file
        Path hiddenFile = testDirectory.resolve(".hiddenFile");
        Files.createFile(hiddenFile);

        listCommand.execute(new String[]{testDirectory.toString()});

        String output = outputStreamCaptor.toString();
        assertFalse(output.contains(".hiddenFile"), "Output should not list the hidden file.");
    }

    @Test
    public void testExecuteListsAllFiles() throws IOException {
        // Create a hidden file
        Path hiddenFile = testDirectory.resolve(".hiddenFile");
        Files.createFile(hiddenFile);
        // Create a visible file
        Path visibleFile = testDirectory.resolve("visibleFile.txt");
        Files.createFile(visibleFile);

        listCommand.execute(new String[]{"-a", testDirectory.toString()});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains(".hiddenFile"), "Output should list the hidden file when -a flag is used.");
        assertTrue(output.contains("visibleFile.txt"), "Output should list the visible file.");
    }

    @Test
    public void testExecuteWithInvalidDirectory() {
        listCommand.execute(new String[]{"invalidDir"});

        String output = outputStreamCaptor.toString();
        assertEquals("Invalid directory: invalidDir", output.trim(), "Output should indicate the directory is invalid.");
    }

    @Test
    public void testExecuteListsFilesRecursively() throws IOException {
        // Create a subdirectory and a file inside it
        Path subDir = testDirectory.resolve("subDir");
        Files.createDirectory(subDir);
        Files.createFile(subDir.resolve("subFile.txt"));

        listCommand.execute(new String[]{"-r", testDirectory.toString()});

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("d subDir"), "Output should list the subdirectory.");
        assertTrue(output.contains("- subFile.txt"), "Output should list the file in the subdirectory.");
    }

    @Test
    public void testExecuteRecursivelyWithHiddenFile() throws IOException {
        // Create a subdirectory and a hidden file inside it
        Path subDir = testDirectory.resolve("subDir");
        Files.createDirectory(subDir);
        Files.createFile(subDir.resolve(".hiddenSubFile"));

        listCommand.execute(new String[]{"-r", testDirectory.toString()});

        String output = outputStreamCaptor.toString();
        assertFalse(output.contains(".hiddenSubFile"), "Output should not list the hidden file in the subdirectory.");
    }
}
