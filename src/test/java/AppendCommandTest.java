import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        Main.handleRedirection(input);

        String content = Files.readString(Paths.get(TEST_FILE));
        assertTrue(content.contains("Test output"));
    }

    @Test
    public void testAppendCommandWithMultipleAppends() throws IOException {
        Main.handleRedirection("echo First line >> " + TEST_FILE);
        Main.handleRedirection("echo Second line >> " + TEST_FILE);

        String content = Files.readString(Paths.get(TEST_FILE));
        assertTrue(content.contains("First line"));
        assertTrue(content.contains("Second line"));
    }

    @Test
    public void testAppendCommandInvalidArgs() {
        AppendCommand appendCommand = new AppendCommand();
        appendCommand.execute(new String[] { "echo" });
    }

    @Test
    public void testAppendCommandInvalidFile() {
        String invalidFile = "/invalid/test.txt";
        String input = "echo Error check >> " + invalidFile;

        Main.handleRedirection(input);
    }
}