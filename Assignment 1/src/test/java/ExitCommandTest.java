import org.junit.jupiter.api.Test;
import org.os.commands.ExitCommand;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ExitCommandTest {
    @Test
    public void testExecuteDoesNotThrowException() {
        ExitCommand exitCommand = new ExitCommand();

        // We expect no exceptions to be thrown when execute is called
        assertDoesNotThrow(() -> exitCommand.execute(new String[]{}));
    }
}

