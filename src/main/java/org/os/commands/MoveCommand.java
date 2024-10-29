package org.os.commands;

import java.io.IOException;
import java.io.File;
import java.util.Arrays;

public class MoveCommand implements Command{
    @Override
    public void execute(String[] args){
        if (args.length == 0) {
            System.out.println("Error: No file name provided.");
            return;
        }

        // start from here :

        // The destination is the last argument
        String destinationPath = args[args.length - 1];
        String[] sourcePaths = Arrays.copyOfRange(args, 0, args.length - 1);

        // Determine OS-specific command
        String moveCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "cmd.exe" : "mv";

        for (String sourcePath : sourcePaths) {
            try {
                // Check if destination is a file or directory
                File destinationFile = new File(destinationPath);
                boolean isRename = sourcePaths.length == 1 && !destinationFile.isDirectory();

                ProcessBuilder processBuilder;
                if (moveCommand.equals("cmd.exe")) {
                    // Windows command
                    processBuilder = new ProcessBuilder(moveCommand, "/c", "move", sourcePath, destinationPath);
                } else {
                    // Unix-like system command
                    processBuilder = new ProcessBuilder(moveCommand, sourcePath, destinationPath);
                }

                // Start the process
                Process process = processBuilder.start();
                int exitCode = process.waitFor();

                // Display appropriate message based on operation type
                if (exitCode == 0) {
                    if (isRename) {
                        System.out.println("Renamed successfully from " + sourcePath + " to " + destinationPath);
                    } else {
                        System.out.println("Moved successfully: " + sourcePath + " to " + destinationPath);
                    }
                } else {
                    System.err.println("Failed to move or rename " + sourcePath + ". Exit code: " + exitCode);
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Error executing move command for " + sourcePath + ": " + e.getMessage());
            }
        }
    }
}

