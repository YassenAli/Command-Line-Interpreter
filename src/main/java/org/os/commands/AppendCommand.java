package org.os.commands;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class AppendCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: command >> filename");
            return;
        }

        String filename = args[args.length - 1];
        String[] commandArgs = Arrays.copyOf(args, args.length - 1);

        String os = System.getProperty("os.name").toLowerCase();
        String command = os.contains("win") ? "cmd.exe" : commandArgs[0];
        if (commandArgs[0].equals("ls")) {
            commandArgs[0] = "dir";
        }

        ProcessBuilder processBuilder;

        try {
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("cmd.exe", "/c", String.join(" ", commandArgs));
            } else {
                processBuilder = new ProcessBuilder(commandArgs);
            }

            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Output appended to " + filename);
            } else {
                System.err.println("Command exited with code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
