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

        // Determine OS-specific command
        String os = System.getProperty("os.name").toLowerCase();
        String command = os.contains("win") ? "cmd.exe" : commandArgs[0]; // Assuming the first argument is the command
        if (commandArgs[0].equals("ls")) {
            // Map 'ls' to 'dir' for Windows
            commandArgs[0] = "dir";
        }

        ProcessBuilder processBuilder;

        try {
            // For Windows, we need to use cmd to run the command
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("cmd.exe", "/c", String.join(" ", commandArgs));
            } else {
                // For Unix-like systems
                processBuilder = new ProcessBuilder(commandArgs);
            }

            // Start the process and get the output
            Process process = processBuilder.start();

            // Append the output from the command to the specified file
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) { // Set append mode to true
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine(); // Write line by line
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

/*
public class AppendCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: <command> >> <filename>");
            return;
        }

        String commandOutput = args[0];
        String filename = args[2];

        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(commandOutput);
        } catch (IOException e) {
            System.out.println("Error appending to file: " + filename);
        }
    }
}*/
