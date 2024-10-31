import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.Main;
import org.os.commands.TouchCommand;

class TouchCommandTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Set up a temporary directory for testing
        tempDir = Files.createTempDirectory("touchCommandTest");
        // Set the current directory in the Main class to the temporary directory
        Main.currentDirectory = tempDir.toString(); // Ensure TouchCommand uses the temp directory
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the temp directory after each test
        Files.walk(tempDir)
                .sorted((path1, path2) -> path2.compareTo(path1)) // Sort to delete files before directories
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    void testCreateNewFile() {
        String fileName = "newFile.txt";
        Path filePath = tempDir.resolve(fileName); // Get the path of the temporary path

        TouchCommand touchCommand = new TouchCommand(); // Call the command touch
        touchCommand.execute(new String[] { fileName }); // The file name that we want to create

        assertTrue(Files.exists(filePath), "File should be created");
        assertTrue(Files.isRegularFile(filePath), "The created file should be a regular file");
        assertEquals(0, filePath.toFile().length(), "File should be empty");
    }

    @Test
    void testFileAlreadyExists() throws IOException {
        // Create a file before running the command
        String fileName = "existingFile.txt";
        Path filePath = Files.createFile(tempDir.resolve(fileName));

        TouchCommand touchCommand = new TouchCommand();
        touchCommand.execute(new String[] { fileName });

        assertTrue(Files.exists(filePath), "File should still exist after touch command is called");
    }

    @Test
    void testInvalidInputNoFileName() {
        TouchCommand touchCommand = new TouchCommand();

        // Capture console output for verification
        String[] args = new String[] {}; // No input has been entered (invalid input)
        touchCommand.execute(args);
    }

    @Test
    void testCreateFileInNestedDirectories() {
        String filePathString = "nested/directory/file.txt";
        Path filePath = tempDir.resolve(filePathString);

        TouchCommand touchCommand = new TouchCommand();
        touchCommand.execute(new String[] { filePathString });

        assertTrue(Files.exists(filePath), "File should be created in the nested directory");
        assertTrue(Files.isRegularFile(filePath), "The created file should be a regular file");
    }
}