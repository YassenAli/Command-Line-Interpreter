//
import org.junit.jupiter.api.*;
import org.os.commands.RemoveDirectoryCommand;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RemoveDirectoryCommandTest {

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
    void TestRemoveDirectory() throws IOException{
        String direname = "emptyDire" ;
        Path direPath = Files.createDirectory(tempDir.resolve(direname));

        assertTrue(Files.exists(direPath) , "Directory should be existed before remove process!");

        RemoveDirectoryCommand removeDirectoryCommand = new RemoveDirectoryCommand();
        removeDirectoryCommand.execute(new String[]{direname});

        assertFalse(Files.exists(direPath) , "The directory should be removed");

    }

    @Test
    void TestRemoveNonExistingDirectory(){

        String direName = "direName" ;
        Path direPath = tempDir.resolve(direName);  //try to find the non-existing dire

        assertFalse(Files.exists(direPath) , "The directory shouldn't be exist");

        RemoveDirectoryCommand removeDirectoryCommand = new RemoveDirectoryCommand();
        removeDirectoryCommand.execute(new String[]{direName});

        assertFalse(Files.exists(direPath) , "The file should be removed");
    }
    @Test
    void TestUnVaildName(){
        RemoveDirectoryCommand removeDirectoryCommand = new RemoveDirectoryCommand();
        removeDirectoryCommand.execute(new String[]{});
    }

    @Test
    void testRemoveNonEmptyDirectory() throws IOException{
        String direName = "direName" ;
        String fileName = "file.txt";
        Path dirPath = Files.createDirectory(tempDir.resolve(direName));
        Files.createFile(dirPath.resolve(fileName));

        RemoveDirectoryCommand removeDirCommand = new RemoveDirectoryCommand();
        removeDirCommand.execute(new String[]{direName});

        assertTrue(Files.exists(dirPath), "Non-empty directory should not be removed");
    }

}