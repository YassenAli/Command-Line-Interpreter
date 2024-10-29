import org.cmd.commands.PipeCommand;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PipeCommandTest {

    @Test
    void testPipeCommand() {
        PipeCommand pipeCommand = new PipeCommand();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Simulate piping "echo Hello" to "cat" command
        pipeCommand.execute(new String[]{"echo Hello", "cat"});

        String output = outputStream.toString().trim();
        assertTrue(output.contains("Hello"));
        System.setOut(System.out);  // Reset to original System.out
    }
}