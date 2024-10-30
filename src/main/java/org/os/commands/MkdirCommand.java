package org.os.commands;

import org.os.Main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MkdirCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: mkdir <directory-name(s)>");
            return;
        }

        Path currentPath = Paths.get(Main.currentDirectory);

        for (String directoryName : args) {
            Path newDirectoryPath = currentPath.resolve(directoryName);

            // Check if the parent directory exists
            if (newDirectoryPath.getParent() != null && !Files.exists(newDirectoryPath.getParent())) {
                System.out.println("Parent directory does not exist: " + newDirectoryPath.getParent());
                continue;
            }

            // Check if a file with the same name exists
            if (Files.exists(newDirectoryPath) && !Files.isDirectory(newDirectoryPath)) {
                System.out.println("A file with the same name exists: " + newDirectoryPath.getFileName());
                continue;
            }

            try {
                Files.createDirectory(newDirectoryPath);
                System.out.println("Directory created: " + newDirectoryPath);
            } catch (IOException e) {
                System.out.println("Error creating directory: " + e.getMessage());
            }
        }
    }
}
