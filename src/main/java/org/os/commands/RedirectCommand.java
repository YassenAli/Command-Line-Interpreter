package org.os.commands;

import java.io.FileWriter;
import java.io.IOException;

public class RedirectCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: <command> > <filename>");
            return;
        }

        String commandOutput = args[0];
        String filename = args[2];

        try (FileWriter writer = new FileWriter(filename, false)) {
            writer.write(commandOutput);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }
    }
}
