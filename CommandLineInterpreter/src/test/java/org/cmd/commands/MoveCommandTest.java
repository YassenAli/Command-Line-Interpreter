package org.cmd.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class MoveCommandTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Set up a temporary directory for testing
        tempDir = Files.createTempDirectory("moveCommandTest");
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
    void TestMoveFile() throws IOException {
        // Create a single file to move
        Path sourceFile = Files.createFile(tempDir.resolve("sourceFile.txt"));
        Path destinationDir = Files.createDirectory(tempDir.resolve("destinationDir"));

        // Execute the move command
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{sourceFile.toString(), destinationDir.toString()});

        // Verify the file was moved
        assertFalse(Files.exists(sourceFile), "Source file should no longer exist");
        assertTrue(Files.exists(destinationDir.resolve("sourceFile.txt")), "File should be in destination directory");
    }

    @Test
    void TestRenameFile() throws IOException {
        // Create a file to rename
        Path sourceFile = Files.createFile(tempDir.resolve("oldName.txt"));
        Path renamedFile = tempDir.resolve("newName.txt");

        // Execute the rename command
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{sourceFile.toString(), renamedFile.toString()});

        // Verify the file was renamed
        assertFalse(Files.exists(sourceFile), "Old file should no longer exist");
        assertTrue(Files.exists(renamedFile), "File should be renamed to the new name");
    }
    @Test
    void testNoFileNameProvided() {
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{});
    }

    @Test
    void testMoveMultipleFilesToDirectory() throws IOException {
        // Create multiple files to move
        Path sourceFile1 = Files.createFile(tempDir.resolve("sourceFile1.txt"));
        Path sourceFile2 = Files.createFile(tempDir.resolve("sourceFile2.txt"));
        Path destinationDir = Files.createDirectory(tempDir.resolve("destinationDir"));

        // Execute the move command
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{sourceFile1.toString(), sourceFile2.toString(), destinationDir.toString()});

        // Verify the files were moved
        assertFalse(Files.exists(sourceFile1), "First source file should no longer exist");
        assertFalse(Files.exists(sourceFile2), "Second source file should no longer exist");
        assertTrue(Files.exists(destinationDir.resolve("sourceFile1.txt")), "First file should be in destination directory");
        assertTrue(Files.exists(destinationDir.resolve("sourceFile2.txt")), "Second file should be in destination directory");
    }

    @Test
    void testMoveNonExistentFile() {
        // Specify a non-existent file
        String nonExistentFile = tempDir.resolve("nonExistentFile.txt").toString();
        Path destinationDir = tempDir.resolve("destinationDir");

        // Create the destination directory
        try {
            Files.createDirectory(destinationDir);
        } catch (IOException e) {
            fail("Could not create destination directory");
        }

        // Execute the move command
        MoveCommand moveCommand = new MoveCommand();
        moveCommand.execute(new String[]{nonExistentFile, destinationDir.toString()});

        // Verify that the file does not exist in the destination
        assertFalse(Files.exists(destinationDir.resolve("nonExistentFile.txt")), "Non-existent file should not appear in destination");
    }
}