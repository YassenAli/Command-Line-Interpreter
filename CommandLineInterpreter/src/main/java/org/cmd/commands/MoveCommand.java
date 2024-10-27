package org.cmd.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MoveCommand implements Command{
    @Override
    public void execute(String[] args){
        if (args.length == 0) {
            System.out.println("Error: No file name provided.");
            return;
        }
        String fileName = args[0];
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);

        try {
            Files.delete(filePath);
            System.out.println("Directory deleted: " + filePath);
        } catch (IOException e) {
            System.out.println("Error deleting directory: " + e.getMessage());
        }
    }
}

