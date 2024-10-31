import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.Main;
import org.os.commands.PipeCommand;

public class PipeCommandTest {
    private PipeCommand pipeCommand;

    @BeforeEach
    public void setUp() {
        pipeCommand = new PipeCommand();
        createTestFiles();
    }

    private void createTestFiles() {
        try {
            File dir = new File("testDir");
            if (!dir.exists()) {
                dir.mkdir();
            }
            new File("testDir/file1.txt").createNewFile();
            new File("testDir/file2.txt").createNewFile();
            try (FileWriter writer = new FileWriter("testDir/file3.txt")) {
                writer.write("file3\nfile3\nx.txt\nx.txt\ndir1\n");
            }
            Main.currentDirectory = "testDir";
        } catch (IOException e) {
            System.err.println("Failed to set up test files: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        deleteTestFiles();
    }

    private void deleteTestFiles() {
        File dir = new File("testDir");
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete(); // Delete each file
                }
            }
            dir.delete(); // Delete the directory itself
        }
    }

    @Test
    public void testLsCommand() throws IOException {
        String[] args = { "ls" };
        List<String> expectedOutput = Arrays.asList("file1.txt", "file2.txt", "file3.txt");

        List<String> output = pipeCommand.executeCommand("ls", args, null);

        assertEquals(expectedOutput, output, "The ls command should list the files in the directory");
    }

    @Test
    public void testSortCommand() throws IOException {
        String[] args = { "sort" };
        List<String> input = Arrays.asList("x.txt", "file3", "file1.txt", "file2.txt");
        List<String> expectedOutput = Arrays.asList("file1.txt", "file2.txt", "file3", "x.txt");

        List<String> output = pipeCommand.executeCommand("sort", args, input);

        assertEquals(expectedOutput, output, "The sort command should sort the input alphabetically");
    }

    @Test
    public void testUniqCommand() throws IOException {
        String[] args = { "uniq" };
        List<String> input = Arrays.asList("file3", "file3", "x.txt", "x.txt", "dir1");
        List<String> expectedOutput = Arrays.asList("file3", "x.txt", "dir1");

        List<String> output = pipeCommand.executeCommand("uniq", args, input);

        assertEquals(expectedOutput, output, "The uniq command should remove consecutive duplicates");
    }
}
