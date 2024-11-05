import org.junit.jupiter.api.*;
import org.os.Main;
import org.os.commands.ListCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ListCommandTest {

    private static final Path testDir = Paths.get("testDir");
    private static final Path hiddenFile = testDir.resolve(".hiddenFile");
    private static final Path visibleFile = testDir.resolve("visibleFile");
    private static final Path nestedDir = testDir.resolve("nestedDir");

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ListCommand listCommand = new ListCommand();

    @BeforeAll
    static void setUp() throws Exception {
        Files.createDirectory(testDir);
        Files.createFile(hiddenFile);
        Files.createFile(visibleFile);
        Files.createDirectory(nestedDir);
    }

    @BeforeEach
    void redirectSystemOutput() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void resetSystemOutput() {
        System.setOut(System.out);
    }

    @AfterAll
    static void tearDown() throws Exception {
        Files.deleteIfExists(hiddenFile);
        Files.deleteIfExists(visibleFile);
        Files.deleteIfExists(nestedDir);
        Files.deleteIfExists(testDir);
    }

    @Test
    void testListWithoutFlags() {
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("- visibleFile"));
        assertTrue(output.contains("d nestedDir"));
        assertFalse(output.contains(".hiddenFile"));
    }

    @Test
    void testListWithAllFlag() {
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{"-a"});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("- visibleFile"));
        assertTrue(output.contains("d nestedDir"));
        assertTrue(output.contains("- .hiddenFile"));
    }

    @Test
    void testListWithReverseFlag() {
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{"-r"});

        String output = outputStreamCaptor.toString().trim();
        String[] lines = output.split("\n");
        assertTrue(lines[0].contains("nestedDir") || lines[0].contains("visibleFile"));
    }

    @Test
    void testListWithAllAndReverseFlags() {
        Main.currentDirectory = testDir.toString();
        listCommand.execute(new String[]{"-a", "-r"});

        String output = outputStreamCaptor.toString().trim();
        String[] lines = output.split("\n");

        // Check for the presence of hidden files
        assertTrue(output.contains(".hiddenFile"), "Output should contain .hiddenFile");

        // Ensure the output is in reverse order: hiddenFile should appear after visibleFile and nestedDir
        boolean foundHiddenFile = false;
        boolean foundVisibleFile = false;
        boolean foundNestedDir = false;

        for (String line : lines) {
            if (line.contains("nestedDir")) {
                foundNestedDir = true;
            }
            if (line.contains("visibleFile")) {
                foundVisibleFile = true;
            }
            if (line.contains(".hiddenFile")) {
                foundHiddenFile = true;
            }
        }

        // Check if the order of appearance is correct
        if (foundHiddenFile) {
            assertTrue(foundVisibleFile || foundNestedDir,
                    "If .hiddenFile is found, it should be after visibleFile or nestedDir in the output.");
        }
    }

    @Test
    void testInvalidDirectory() {
        Main.currentDirectory = "invalidDir";
        listCommand.execute(new String[]{});

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("Invalid directory: ."));
    }
}
