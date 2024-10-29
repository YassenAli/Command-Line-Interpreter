package org.cmd.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MkdirCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: mkdir <directory-name>");
            return;
        }

        String directoryName = args[0];
        Path path = Paths.get(directoryName);

        // Check if the parent directory exists
        if (path.getParent() != null && !Files.exists(path.getParent())) {
            System.out.println("Parent directory does not exist: " + path.getParent());
            return;
        }

        // Check if a file with the same name exists
        if (Files.exists(path) && !Files.isDirectory(path)) {
            System.out.println("A file with the same name exists: " + path.getFileName());
            return;
        }

        try {
            Files.createDirectory(path);
            System.out.println("Directory created: " + path.toString());
        } catch (IOException e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }
    }
}
