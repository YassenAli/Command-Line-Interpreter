package org.cmd.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TouchCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: No file name provided.");
            return;
        }

        String fileName = args[0];
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);

        try {
            // Create the parent directories if they do not exist
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            if (Files.exists(filePath)) {
                System.out.println("File already exists: " + fileName);
            } else {
                Files.createFile(filePath);
                System.out.println("File created: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }
}
