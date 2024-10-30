//package org.os.commands;
//
//import java.io.IOException;
//import java.io.File;
//import java.util.Arrays;
//
//public class MoveCommand implements Command{
//    @Override
//    public void execute(String[] args){
//        if (args.length == 0) {
//            System.out.println("Error: No file name provided.");
//            return;
//        }
//
//        // start from here :
//
//        // The destination is the last argument
//        String destinationPath = args[args.length - 1];
//        String[] sourcePaths = Arrays.copyOfRange(args, 0, args.length - 1);
//
//        // Determine OS-specific command
//        String moveCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "cmd.exe" : "mv";
//
//        for (String sourcePath : sourcePaths) {
//            try {
//                // Check if destination is a file or directory
//                File destinationFile = new File(destinationPath);
//                boolean isRename = sourcePaths.length == 1 && !destinationFile.isDirectory();
//
//                ProcessBuilder processBuilder;
//                if (moveCommand.equals("cmd.exe")) {
//                    // Windows command
//                    processBuilder = new ProcessBuilder(moveCommand, "/c", "move", sourcePath, destinationPath);
//                } else {
//                    // Unix-like system command
//                    processBuilder = new ProcessBuilder(moveCommand, sourcePath, destinationPath);
//                }
//
//                // Start the process
//                Process process = processBuilder.start();
//                int exitCode = process.waitFor();
//
//                // Display appropriate message based on operation type
//                if (exitCode == 0) {
//                    if (isRename) {
//                        System.out.println("Renamed successfully from " + sourcePath + " to " + destinationPath);
//                    } else {
//                        System.out.println("Moved successfully: " + sourcePath + " to " + destinationPath);
//                    }
//                } else {
//                    System.err.println("Failed to move or rename " + sourcePath + ". Exit code: " + exitCode);
//                }
//
//            } catch (IOException | InterruptedException e) {
//                System.err.println("Error executing move command for " + sourcePath + ": " + e.getMessage());
//            }
//        }
//    }
//}
//
package org.os.commands;

import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import org.os.Main; // Import Main to access the current directory

public class MoveCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: No file name provided.");
            return;
        }

        // Get current directory from Main
        String currentDirectory = Main.currentDirectory;

        // Destination is the last argument
        String destinationPath = args[args.length - 1];
        String[] sourcePaths = Arrays.copyOfRange(args, 0, args.length - 1);

        // Full path for the destination
        File destinationFile = new File(currentDirectory, destinationPath);
        String destinationFullPath = destinationFile.getAbsolutePath();

        // Determine OS-specific command
        String moveCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "cmd.exe" : "mv";

        for (String sourcePath : sourcePaths) {
            try {
                // Create a full path for the source file
                File sourceFile = new File(currentDirectory, sourcePath);
                String sourceFullPath = sourceFile.getAbsolutePath();

                boolean isRename = sourcePaths.length == 1 && !destinationFile.isDirectory();

                ProcessBuilder processBuilder;
                if (moveCommand.equals("cmd.exe")) {
                    // Windows command
                    processBuilder = new ProcessBuilder(moveCommand, "/c", "move", sourceFullPath, destinationFullPath);
                } else {
                    // Unix-like system command
                    processBuilder = new ProcessBuilder(moveCommand, sourceFullPath, destinationFullPath);
                }

                // Start the process
                Process process = processBuilder.start();
                int exitCode = process.waitFor();

                // Display appropriate message based on operation type
                if (exitCode == 0) {
                    if (isRename) {
                        System.out.println("Renamed successfully from " + sourceFullPath + " to " + destinationFullPath);
                    } else {
                        System.out.println("Moved successfully: " + sourceFullPath + " to " + destinationFullPath);
                    }
                } else {
                    System.err.println("Failed to move or rename " + sourceFullPath + ". Exit code: " + exitCode);
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("Error executing move command for " + sourcePath + ": " + e.getMessage());
            }
        }
    }
}
