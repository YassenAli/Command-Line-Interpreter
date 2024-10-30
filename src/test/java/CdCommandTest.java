import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.Main;
import org.os.commands.CdCommand;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CdCommandTest {
    private CdCommand cdCommand;

    @BeforeEach
    void setUp() {
        cdCommand = new CdCommand();
        Main.currentDirectory = System.getProperty("user.dir"); // Set starting directory for tests
    }

    @Test
    void testChangeToSpecificDirectory() {
        String[] args = {"src"};
        cdCommand.execute(args);

        String expectedPath = Paths.get(System.getProperty("user.dir")).resolve("src").normalize().toString();
        assertEquals(expectedPath, Main.currentDirectory, "Should change to specified directory");
    }

    @Test
    void testChangeToParentDirectory() {
        String startingDir = Paths.get(Main.currentDirectory).toString();
        String[] args = {".."};

        cdCommand.execute(args);

        String expectedPath = Paths.get(startingDir).getParent().toString();
        assertEquals(expectedPath, Main.currentDirectory, "Should change to parent directory");
    }

    @Test
    void testChangeToHomeDirectory() {
        String[] args = {"~"};
        cdCommand.execute(args);

        String expectedPath = System.getProperty("user.home");
        assertEquals(expectedPath, Main.currentDirectory, "Should change to home directory");
    }

    @Test
    void testInvalidDirectory() {
        String[] args = {"nonexistentDirectory"};
        cdCommand.execute(args);

        String expectedPath = System.getProperty("user.dir"); // Should remain in current directory
        assertEquals(expectedPath, Main.currentDirectory, "Should stay in current directory on invalid path");
    }

    @Test
    void testNoArgumentProvided() {
        String[] args = {};
        cdCommand.execute(args);

        String expectedPath = System.getProperty("user.dir"); // Should remain in current directory
        assertEquals(expectedPath, Main.currentDirectory, "Should stay in current directory on no arguments");
    }
}
