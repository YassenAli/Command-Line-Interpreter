package org.os.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.os.Main;

public class PipeCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: command1 | command2 | ...");
            return;
        }

        String[] commands = Arrays.stream(args).map(String::trim).toArray(String[]::new);
        List<String> intermediateOutput = null;

        try {
            for (String commandWithArgs : commands) {
                String[] commandParts = commandWithArgs.split("\\s+");
                String command = commandParts[0].toLowerCase();
                String[] commandArgs = Arrays.copyOfRange(commandParts, 1, commandParts.length);

                intermediateOutput = executeCommand(command, commandArgs, intermediateOutput);
            }

            if (intermediateOutput != null) {
                for (String line : intermediateOutput) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error executing pipe command: " + e.getMessage());
        }
    }

    public List<String> executeCommand(String command, String[] args, List<String> input) throws IOException {
        List<String> output = new ArrayList<>();

        switch (command) {
            case "ls":
                output.addAll(listDirectory());
                break;
            case "sort":
                if (input != null) {
                    output.addAll(sort(input));
                } else {
                    System.out.println("Sort command requires input from a previous command.");
                }
                break;
            case "uniq":
                if (input != null) {
                    output.addAll(uniq(input));
                } else {
                    System.out.println("Uniq command requires input from a previous command.");
                }
                break;
            case "grep":
                if (input != null && args.length > 0) {
                    output.addAll(grep(input, args[0]));
                } else {
                    System.out.println("Grep command requires input and a pattern to search for.");
                }
                break;
            default:
                System.out.println("Invalid or unsupported command in pipe: " + command);
        }

        return output;
    }

    private List<String> listDirectory() {
        List<String> result = new ArrayList<>();
        File currentDir = new File(Main.currentDirectory);
        File[] filesList = currentDir.listFiles();

        if (filesList != null) {
            for (File file : filesList) {
                result.add(file.getName());
            }
        } else {
            System.out.println("Could not list directory contents.");
        }

        return result;
    }

    private List<String> sort(List<String> input) {
        List<String> sorted = new ArrayList<>(input);
        Collections.sort(sorted);
        return sorted;
    }

    private List<String> uniq(List<String> input) {
        List<String> unique = new ArrayList<>();
        String previous = null;

        for (String line : input) {
            if (!line.equals(previous)) {
                unique.add(line);
                previous = line;
            }
        }

        return unique;
    }

    private List<String> grep(List<String> input, String pattern) {
        List<String> result = new ArrayList<>();

        for (String line : input) {
            if (line.contains(pattern)) {
                result.add(line);
            }
        }

        return result;
    }
}