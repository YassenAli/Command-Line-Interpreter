package org.os.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import org.os.Main;

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

        for (String sourcePath : sourcePaths) {
            try {
                // Create a full path for the source file
                File sourceFile = new File(currentDirectory, sourcePath);

                // Determine final destination
                File finalDestination;
                if (destinationFile.isDirectory()) {
                    // Move to directory with original file name
                    finalDestination = new File(destinationFile, sourceFile.getName());
                } else {
                    // Destination is a file (for renaming or overwriting)
                    finalDestination = destinationFile;
                }

                // Move the file using Java NIO
                Files.move(sourceFile.toPath(), finalDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Display appropriate message based on operation type
                if (sourcePaths.length == 1 && !destinationFile.isDirectory()) {
                    System.out.println("Renamed successfully from " + sourceFile.getAbsolutePath() + " to " + finalDestination.getAbsolutePath());
                } else {
                    System.out.println("Moved successfully: " + sourceFile.getAbsolutePath() + " to " + finalDestination.getAbsolutePath());
                }

            } catch (IOException e) {
                System.err.println("Error moving or renaming file: " + sourcePath + " - " + e.getMessage());
            }
        }
    }
}
