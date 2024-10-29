import org.junit.jupiter.api.*;
import org.os.commands.*;
import org.os.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class MkdirCommandTest {
    private MkdirCommand mkdirCommand;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Path testDirectory;

    @BeforeEach
    public void setUp() {
        mkdirCommand = new MkdirCommand();
        testDirectory = Paths.get("testDir");
        System.setOut(new PrintStream(outputStreamCaptor));

        // Clean up before each test
        try {
            if (Files.exists(testDirectory)) {
                Files.delete(testDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test
        try {
            if (Files.exists(testDirectory)) {
                Files.delete(testDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(originalOut);
    }

    @Test
    public void testExecuteCreatesDirectory() {
        mkdirCommand.execute(new String[]{"testDir"});
        assertTrue(Files.exists(testDirectory), "Directory should be created.");
        assertEquals("Directory created: testDir", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testExecuteWithNoArguments() {
        mkdirCommand.execute(new String[]{});
        assertEquals("Usage: mkdir <directory-name>", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testExecuteWithNonExistentParentDirectory() {
        Path nonExistentParent = Paths.get("nonExistentDir/testDir");
        mkdirCommand.execute(new String[]{"nonExistentDir/testDir"});
        assertEquals("Parent directory does not exist: nonExistentDir", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testExecuteWithFileExisting() throws IOException {
        // Create a file with the same name
        Path existingFile = Paths.get("existingFile");
        Files.createFile(existingFile);

        mkdirCommand.execute(new String[]{"existingFile"});
        assertEquals("A file with the same name exists: existingFile", outputStreamCaptor.toString().trim());

        // Clean up
        Files.delete(existingFile);
    }

    @Test
    public void testCommandFactoryIntegration() {
        Command command = CommandFactory.getCommand("mkdir");
        assertNotNull(command, "Command should be instantiated by CommandFactory");

        // Simulate command execution through Main
        String input = "mkdir testDir\nexit\n"; // Input to simulate
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Capture output
        try {
            Main.main(new String[]{});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(outputStreamCaptor.toString().contains("Directory created: testDir"),
                "Output should indicate the directory was created through Main.");
    }
}
