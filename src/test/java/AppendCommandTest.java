import org.os.commands.AppendCommand;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class AppendCommandTest {

    private final String filename = "test_append.txt";

    @BeforeEach
    void setup() throws IOException {
        Files.writeString(new File(filename).toPath(), "Initial Line\n");
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(new File(filename).toPath());
    }

    @Test
    void testAppendCommand() throws IOException {
        AppendCommand appendCommand = new AppendCommand();
        appendCommand.execute(new String[]{"Appended Line", filename});

        File file = new File(filename);
        assertTrue(file.exists());
        String content = Files.readString(file.toPath());
        assertEquals("Initial Line\nAppended Line", content.trim());
    }
}