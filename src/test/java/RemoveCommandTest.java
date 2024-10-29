import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.os.commands.RemoveCommand;

import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class RemoveCommandTest {

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
    void TestRemoveExistingFile() throws IOException{
        String fileName = "fileToRemove.txt";
        Path filePath = Files.createFile(tempDir.resolve(fileName)); //create the file we want to remove

        assertTrue(Files.exists(filePath) , "File should exist before removal");

        RemoveCommand removeCommand = new RemoveCommand();
        removeCommand.execute(new String[]{fileName});

        assertTrue(!Files.exists(filePath) , "The file should be removed!");

    }

    @Test
    void TestRemoveNonExistFile(){
        String fileName = "fileToRemove.txt";
        Path filePath = tempDir.resolve(fileName);

        assertFalse(Files.exists(filePath) , "The file should not be exist before the remove process");

        RemoveCommand removeCommand = new RemoveCommand();
        removeCommand.execute(new String[]{fileName});

        // Confirm no file was created or removed
        assertFalse(Files.exists(filePath), "File should still not exist");
    }

    @Test
    void TestUnValidName(){
        RemoveCommand removeCommand = new RemoveCommand();
        removeCommand.execute(new String[]{}); // no name provided
    }

}