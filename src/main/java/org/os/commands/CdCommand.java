package org.os.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import org.os.Main; // Import Main to access its static fields

public class CdCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: cd <directory>");
            return;
        }

        String newPath = String.join(" " , args);
        Path targetPath;

        // Handle ".." to navigate to parent directory
        if (newPath.equals("..")) {
            targetPath = Paths.get(Main.currentDirectory).getParent();
        } else if (newPath.equals("~")) {
            String homeDir = System.getProperty("user.home");
            targetPath = Paths.get(homeDir);
        } else {
            targetPath = Paths.get(Main.currentDirectory).resolve(newPath).normalize();
        }

        // Check if the target path is valid
        if (targetPath != null && Files.isDirectory(targetPath)) {
            Main.currentDirectory = targetPath.toString(); // Update the actual directory in Main
            System.out.println("Directory changed to: " + Main.currentDirectory);
        } else {
            System.out.println("Invalid directory: " + newPath);
        }
    }
}
