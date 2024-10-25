package commandlineinterprter.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CatCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: cat <filename>");
            return;
        }

        String filename = args[1];
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("File not found or error reading file: " + filename);
        }
    }
}