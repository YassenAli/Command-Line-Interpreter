import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.Main;
import org.os.commands.MoveCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MoveCommandTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary directory for testing
        tempDir = Files.createTempDirectory("moveCommandTest");

        // Set Main.currentDirectory to the temporary test directory
        Main.currentDirectory = tempDir.toString();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up all files and directories within tempDir after each test
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(file -> file.delete());
    }

    @Test
    void testMoveSingleFileToDirectory() throws IOException {
        // Create a single file to move
        Path sourceFile = Files.createFile(tempDir.resolve("sourceFile.txt"));
        Path destinationDir = Files.createDirectory(tempDir.resolve("destinationDir"));

        // Execute the move command with relative paths
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{sourceFile.getFileName().toString(), destinationDir.getFileName().toString()});

        // Verify the file was moved
        assertFalse(Files.exists(sourceFile), "Source file should no longer exist");
        assertTrue(Files.exists(destinationDir.resolve("sourceFile.txt")), "File should be in the destination directory");
    }

    @Test
    void testRenameFile() throws IOException {
        // Create a file to rename
        Path sourceFile = Files.createFile(tempDir.resolve("oldName.txt"));
        Path renamedFile = tempDir.resolve("newName.txt");

        // Execute the rename command with relative paths
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{sourceFile.getFileName().toString(), renamedFile.getFileName().toString()});

        // Verify the file was renamed
        assertFalse(Files.exists(sourceFile), "Old file should no longer exist");
        assertTrue(Files.exists(renamedFile), "File should be renamed to the new name");
    }

    @Test
    void testNoFileNameProvided() {
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{});
        // We would check console output if we needed to validate the error message in a real scenario
    }

    @Test
    void testMoveMultipleFilesToDirectory() throws IOException {
        // Create multiple files to move
        Path sourceFile1 = Files.createFile(tempDir.resolve("sourceFile1.txt"));
        Path sourceFile2 = Files.createFile(tempDir.resolve("sourceFile2.txt"));
        Path destinationDir = Files.createDirectory(tempDir.resolve("destinationDir"));

        // Execute the move command with relative paths
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{
                sourceFile1.getFileName().toString(),
                sourceFile2.getFileName().toString(),
                destinationDir.getFileName().toString()
        });

        // Verify both files were moved
        assertFalse(Files.exists(sourceFile1), "First source file should no longer exist");
        assertFalse(Files.exists(sourceFile2), "Second source file should no longer exist");
        assertTrue(Files.exists(destinationDir.resolve("sourceFile1.txt")), "First file should be in the destination directory");
        assertTrue(Files.exists(destinationDir.resolve("sourceFile2.txt")), "Second file should be in the destination directory");
    }

    @Test
    void testMoveNonExistentFile() {
        // Specify a non-existent file by filename
        String nonExistentFile = "nonExistentFile.txt";
        Path destinationDir = tempDir.resolve("destinationDir");

        try {
            // Create the destination directory
            Files.createDirectory(destinationDir);
        } catch (IOException e) {
            fail("Could not create destination directory");
        }

        // Execute the move command with relative paths
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{nonExistentFile, destinationDir.getFileName().toString()});

        // Verify the non-existent file does not appear in the destination
        assertFalse(Files.exists(destinationDir.resolve(nonExistentFile)), "Non-existent file should not appear in destination");
    }
}

