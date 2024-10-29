import org.junit.jupiter.api.*;
import org.os.commands.TouchCommand;

import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class TouchCommandTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Set up a temporary directory for testing
        tempDir = Files.createTempDirectory("touchCommandTest");
        System.setProperty("user.dir", tempDir.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the temp directory after each test
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(file -> file.delete());
    }

    @Test
    void testCreateNewFile() {
        String fileName = "newFile.txt";
        Path filePath = tempDir.resolve(fileName); //get the path of the temporary path

        TouchCommand touchCommand = new TouchCommand(); // call the command touch
        touchCommand.execute(new String[]{fileName});  //the file name that we want to create

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
        touchCommand.execute(new String[]{fileName});

        assertTrue(Files.exists(filePath), "File should still exist");
    }

    @Test
    void testInvalidInputNoFileName() {
        TouchCommand touchCommand = new TouchCommand();

        // Capture console output for verification
        String[] args = new String[]{}; //no input has been entered (invalid input)
        touchCommand.execute(args);
    }

    @Test
    void testCreateFileInNestedDirectories() {
        String filePathString = "nested/directory/file.txt";
        Path filePath = tempDir.resolve(filePathString);

        TouchCommand touchCommand = new TouchCommand();
        touchCommand.execute(new String[]{filePathString});

        assertTrue(Files.exists(filePath), "File should be created in the nested directory");
        assertTrue(Files.isRegularFile(filePath), "The created file should be a regular file");
    }
}
