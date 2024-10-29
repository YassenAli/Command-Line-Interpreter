import org.cmd.commands.PipeCommand;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CatCommandTest {

    private final String filename = "test_cat.txt";

    @BeforeEach
    void setup() throws Exception {
        Files.writeString(new File(filename).toPath(), "File content for testing.");
    }

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(new File(filename).toPath());
    }

    @Test
    void testCatCommand() {
        CatCommand catCommand = new CatCommand();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        catCommand.execute(new String[]{filename});

        String output = outputStream.toString();
        assertTrue(output.contains("File content for testing."));
        System.setOut(System.out);  // Reset to original System.out
    }
}