import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.commands.CatCommand;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CatCommandTest {
    private final String filename = "test_cat.txt";
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup() throws Exception {
        Files.writeString(new File(filename).toPath(), "File content for testing.");
        System.setOut(new PrintStream(outputStream));  // Capture System.out output
    }

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(new File(filename).toPath());
        System.setOut(originalOut);  // Reset System.out
    }

    @Test
    void testCatCommand() {
        CatCommand catCommand = new CatCommand();
        catCommand.execute(new String[]{filename});

        String output = outputStream.toString().trim();
        assertTrue(output.contains("File content for testing."), "CatCommand should output the file content.");
    }

    @Test
    void testCatCommandFileNotFound() {
        CatCommand catCommand = new CatCommand();
        catCommand.execute(new String[]{"nonexistent.txt"});

        String output = outputStream.toString().trim();
        assertTrue(output.contains("File not found"), "CatCommand should output error for nonexistent file.");
    }
}