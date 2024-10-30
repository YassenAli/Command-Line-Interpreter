

import org.os.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.commands.PwdCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PwdCommandTest {

    private PwdCommand pwdCommand;

    @BeforeEach
    void setUp() {
        pwdCommand = new PwdCommand();
    }

    @Test
    void testExecute() {
        // Test case 1: Check default current directory
        Main.currentDirectory = "/home/user"; // Set the expected directory
        assertOutputEquals("/home/user"); // Verify output

        // Test case 2: Change to another directory
        Main.currentDirectory = "/var/log"; // Change the directory
        assertOutputEquals("/var/log"); // Verify output
    }

    private void assertOutputEquals(String expected) {
        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Execute the command
        pwdCommand.execute(new String[]{});

        // Verify the output
        assertEquals(expected, outputStream.toString().trim());

        // Reset System.out
        System.setOut(System.out);
    }
}