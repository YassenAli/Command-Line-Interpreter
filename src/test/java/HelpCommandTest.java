

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.commands.HelpCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest {

    private HelpCommand helpCommand;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        helpCommand = new HelpCommand();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testExecutePrintsHelpMessages() {
        // Execute the command
        helpCommand.execute(new String[]{});

        // Expected output
        String expectedOutput = String.join(System.lineSeparator(),
                "pwd: print the path of the current directory",
                "cd: change directory",
                "ls: view content of directory",
                "ls -a: List all files, including hidden",
                "ls -r: List files recursively",
                "mkdir: Create a directory",
                "rmdir: Remove a directory",
                "touch: Create an empty file",
                "mv: Move or rename a file",
                "rm: Remove a file",
                "cat: Display file content",
                ">: Redirect output to a file",
                ">>: Append output to a file",
                "|: Pipe output from one command to another"
        );

        // Verify the output
        assertEquals(expectedOutput, outputStream.toString().trim());
    }

}