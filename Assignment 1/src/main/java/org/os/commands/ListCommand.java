package org.os.commands;

import org.os.Main;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListCommand implements Command {
    public void execute(String[] args) {
        boolean listAll = false;
        boolean reverseOrder = false;

        for (String arg : args) {
            if (arg.equals("-a")) {
                listAll = true;
            } else if (arg.equals("-r")) {
                reverseOrder = true;
            }
        }

        String directoryPath = getDirectoryPath(args);
        Path dir = Paths.get(Main.currentDirectory);

        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            System.out.println("Invalid directory: " + directoryPath);
            return;
        }

        listFiles(dir, listAll, reverseOrder);
    }

    private void listFiles(Path dir, boolean listAll, boolean reverseOrder) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            List<Path> fileList = new ArrayList<>();
            stream.forEach(fileList::add);

            // Sort in reverse order if reverseOrder is true
            if (reverseOrder) {
                fileList.sort(Collections.reverseOrder());
            }

            for (Path entry : fileList) {
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
