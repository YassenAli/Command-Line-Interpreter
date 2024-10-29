import org.os.commands.RedirectCommand;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedirectCommandTest {

    private final String filename = "test_output.txt";

    @BeforeEach
    void setup() throws IOException {
        Files.deleteIfExists(new File(filename).toPath());
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(new File(filename).toPath());
    }

    @Test
    void testRedirectCommand() throws IOException {
        RedirectCommand redirectCommand = new RedirectCommand();
        redirectCommand.execute(new String[]{"Hello, World!", filename});

        File file = new File(filename);
        assertTrue(file.exists());
        assertEquals("Hello, World!", Files.readString(file.toPath()).trim());
    }
}