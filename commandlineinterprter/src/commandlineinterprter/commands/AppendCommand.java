package commandlineinterprter.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AppendCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: <command> >> <filename>");
            return;
        }

        String commandOutput = args[1];
        String filename = args[2];

        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(commandOutput);
        } catch (IOException e) {
            System.out.println("Error appending to file: " + filename);
        }
    }
}