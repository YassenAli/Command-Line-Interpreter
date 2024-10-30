package org.os.commands;
import org.os.Main;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.io.File;

public class RedirectCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: command > filename");
            return;
        }

        String filename = args[args.length - 1];
        String command = args[0].toLowerCase();
        String content = String.join(" ", Arrays.copyOfRange(args, 1, args.length - 1));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            switch (command) {
                case "ls":
                    listDirectory(writer);
                    break;
                case "pwd":
                    printWorkingDirectory(writer);
                    break;
                case "echo":
                    writer.write(content);
                    writer.newLine();
                    System.out.println("Text echoed and redirected to file.");
                    break;
                default:
                    System.out.println("Invalid or unsupported command for redirection: " + command);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void listDirectory(BufferedWriter writer) throws IOException {
        File currentDir = new File(Main.currentDirectory);
        File[] filesList = currentDir.listFiles();

        if (filesList != null) {
            for (File file : filesList) {
                writer.write(file.getName());
                writer.newLine();
            }
            System.out.println("Directory listing redirected to file.");
        } else {
            System.out.println("Could not list directory contents.");
        }
    }

    private void printWorkingDirectory(BufferedWriter writer) throws IOException {
        writer.write(Main.currentDirectory);
        writer.newLine();
        System.out.println("Working directory path redirected to file.");
    }
}

/*public class RedirectCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: command > filename");
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
                 BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Output redirected to " + filename);
            } else {
                System.err.println("Command exited with code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}*/
