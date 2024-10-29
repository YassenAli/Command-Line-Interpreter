import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.commands.AppendCommand;

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
        assertTrue(file.exists(), "Output file should be created or exist.");
        String content = Files.readString(file.toPath());
        assertEquals("Initial Line\nAppended Line", content.trim(), "Content should match appended line format.");
    }

    @Test
    void testAppendCommandMultipleAppends() throws IOException {
        AppendCommand appendCommand = new AppendCommand();
        appendCommand.execute(new String[]{"First Append", filename});
        appendCommand.execute(new String[]{"Second Append", filename});

        String content = Files.readString(new File(filename).toPath());
        assertEquals("Initial Line\nFirst Append\nSecond Append", content.trim(), "Should correctly append multiple lines.");
    }

    @Test
    void testAppendCommandFileNotFound() {
        AppendCommand appendCommand = new AppendCommand();
        Exception exception = assertThrows(IOException.class, () -> appendCommand.execute(new String[]{"Missing File Append", "nonexistent.txt"}));
        assertTrue(exception.getMessage().contains("Error"), "Should throw an error when the file is not found.");
    }
}
