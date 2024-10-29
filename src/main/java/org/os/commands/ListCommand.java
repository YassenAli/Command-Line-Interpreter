package org.os.commands;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class ListCommand implements Command { 
    public void execute(String[] args) {
        boolean listAll = false;
        boolean listRecursively = false;

        for (String arg : args) {
            if (arg.equals("-a")) {
                listAll = true;
            } else if (arg.equals("-r")) {
                listRecursively = true;
            }
        }

        String directoryPath = getDirectoryPath(args);
        Path dir = Paths.get(directoryPath);

        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            System.out.println("Invalid directory: " + directoryPath);
            return;
        }

        if (listRecursively) {
            listFilesRecursively(dir, listAll, 0);
        } else {
            listFiles(dir, listAll);
        }
    }

    private void listFiles(Path dir, boolean listAll) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (!listAll && entry.getFileName().toString().startsWith(".")) {
                    continue; // Skip hidden files
                }
                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                String typeIndicator = attrs.isDirectory() ? "d" : "-";
                System.out.println(typeIndicator + " " + entry.getFileName());
            }
        } catch (IOException e) {
            System.out.println("Error listing directory contents: " + e.getMessage());
        }
    }

    private void listFilesRecursively(Path dir, boolean listAll, int level) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (!listAll && entry.getFileName().toString().startsWith(".")) {
                    continue; // Skip hidden files
                }
                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                String typeIndicator = attrs.isDirectory() ? "d" : "-";
                System.out.println("  ".repeat(level) + typeIndicator + " " + entry.getFileName());

                // If it's a directory, list its contents recursively
                if (attrs.isDirectory()) {
                    listFilesRecursively(entry, listAll, level + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Error listing directory contents: " + e.getMessage());
        }
    }

    private String getDirectoryPath(String[] args) {
        if (args.length > 0) {
            StringBuilder pathBuilder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (!args[i].equals("-a") && !args[i].equals("-r")) { // Ignore flags
                    pathBuilder.append(args[i]);
                    if (i < args.length - 1) {
                        pathBuilder.append(" ");
                    }
                }
            }
            return pathBuilder.toString().replace("\"", "").trim();
        } else {
            return ".";
        }
    }
}