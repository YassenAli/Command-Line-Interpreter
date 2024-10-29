import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.os.Main;
import org.os.commands.AppendCommand;

class AppendCommandTest {
    private static final String TEST_FILE = "append_output_test.txt";

    @BeforeEach
    public void setup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
        Files.createFile(Paths.get(TEST_FILE));
    }

    @AfterEach
    public void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    public void testAppendCommand() throws IOException {
        String input = "echo Test output >> " + TEST_FILE;

        // Handle redirection in Main class and execute AppendCommand
        Main.handleRedirection(input);

        // Verify output was appended
        String content = Files.readString(Paths.get(TEST_FILE));
        assertTrue(content.contains("Test output"));
    }

    @Test
    public void testAppendCommandWithMultipleAppends() throws IOException {
        Main.handleRedirection("echo First line >> " + TEST_FILE);
        Main.handleRedirection("echo Second line >> " + TEST_FILE);

        // Check both lines in file
        String content = Files.readString(Paths.get(TEST_FILE));
        assertTrue(content.contains("First line"));
        assertTrue(content.contains("Second line"));
    }

    @Test
    public void testAppendCommandInvalidArgs() {
        AppendCommand appendCommand = new AppendCommand();
        appendCommand.execute(new String[]{"echo"}); // No filename

        // Verify it outputs usage instructions or error handling
    }

    @Test
    public void testAppendCommandInvalidFile() {
        String invalidFile = "/invalid/test.txt";
        String input = "echo Error check >> " + invalidFile;

        // Redirect to invalid file path
        Main.handleRedirection(input);

        // Optionally verify error message output (e.g., using logs or error stream capturing)
    }
}