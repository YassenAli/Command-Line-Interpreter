import org.os.commands.RedirectCommand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedReader;

public class RedirectCommandTest {
    private RedirectCommand redirectCommand;
    private final String testFileName = "test_output.txt";

    @BeforeEach
    void setUp() {
        redirectCommand = new RedirectCommand();
        deleteTestFile();
    }

    private void deleteTestFile() {
        File file = new File(testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testValidCommandRedirection() throws IOException {
        String[] args = {"echo", "Hello, World!", ">", testFileName};
        redirectCommand.execute(args);

        File file = new File(testFileName);
        assertTrue(file.exists(), "Output file should be created.");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            assertEquals("Hello, World!", line, "Output file should contain the command's output.");
        }
    }

    @Test
    void testInvalidCommand() {
        String[] args = {"invalidcommand", ">", testFileName};
        assertThrows(IOException.class, () -> redirectCommand.execute(args), "Invalid command should throw an exception.");
    }

    @Test
    void testMissingFilename() {
        String[] args = {"echo", "Hello"};
        redirectCommand.execute(args);

        File file = new File(testFileName);
        assertFalse(file.exists(), "Output file should not be created if filename is missing.");
    }

    @Test
    void testAppendRedirection() throws IOException {
        String[] initialArgs = {"echo", "First line", ">", testFileName};
        redirectCommand.execute(initialArgs);

        String[] appendArgs = {"echo", "Second line", ">>", testFileName};
        redirectCommand.execute(appendArgs);

        File file = new File(testFileName);
        assertTrue(file.exists(), "Output file should be created.");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertEquals("First line", reader.readLine());
            assertEquals("Second line", reader.readLine());
        }
    }

    @Test
    void testHandleRedirectionInvalidFilename() {
        String[] args = {"echo", "Some content", ">", "/invalid/path/test_output.txt"};
        Exception exception = assertThrows(IOException.class, () -> redirectCommand.execute(args));
        assertTrue(exception.getMessage().contains("Error:"), "Should throw an error for invalid file paths.");
    }

    @Test
    void testHandleEmptyArgs() {
        String[] args = {};
        redirectCommand.execute(args);
        File file = new File(testFileName);
        assertFalse(file.exists(), "No file should be created for empty arguments.");
    }
}