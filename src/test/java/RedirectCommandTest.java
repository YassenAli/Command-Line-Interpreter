import org.os.commands.RedirectCommand;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class RedirectCommandTest {
    private static final String TEST_FILE = "test_output_redirect.txt";

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
    public void testRedirectCommand() throws IOException {
        String[] args = {"echo", "Redirected output"};
        args = addFilenameToArgs(args, TEST_FILE);

        RedirectCommand redirectCommand = new RedirectCommand();
        redirectCommand.execute(args);

        // Check that the file contains the redirected output, and ensure it's not appended
        String content = Files.readString(Paths.get(TEST_FILE));
        assertTrue(content.contains("Redirected output"));
    }

    @Test
    public void testRedirectCommand_Overwrite() throws IOException {
        Files.writeString(Paths.get(TEST_FILE), "Existing content");

        String[] args = {"echo", "New content"};
        args = addFilenameToArgs(args, TEST_FILE);

        RedirectCommand redirectCommand = new RedirectCommand();
        redirectCommand.execute(args);

        // Verify the file was overwritten with "New content"
        String content = Files.readString(Paths.get(TEST_FILE));
        assertTrue(content.contains("New content") && !content.contains("Existing content"),
                "File should be overwritten with new content");
    }

    @Test
    public void testRedirectCommand_InvalidFile() {
        String invalidFilename = "/invalid_path/test.txt";
        String[] args = {"echo", "Some output", invalidFilename};

        RedirectCommand redirectCommand = new RedirectCommand();
        redirectCommand.execute(args);
    }

    private String[] addFilenameToArgs(String[] args, String filename) {
        String[] newArgs = new String[args.length + 1];
        System.arraycopy(args, 0, newArgs, 0, args.length);
        newArgs[args.length] = filename;
        return newArgs;
    }
}