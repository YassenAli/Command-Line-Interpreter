package org.os.commands;
import org.os.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import java.io.*;
import java.util.*;

public class PipeCommand implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: command1 | command2 | ...");
            return;
        }

        // Split commands by pipe
        String[] commands = Arrays.stream(args).map(String::trim).toArray(String[]::new);
        List<String> intermediateOutput = null;

        try {
            for (String commandWithArgs : commands) {
                // Split each command and its arguments
                String[] commandParts = commandWithArgs.split("\\s+");
                String command = commandParts[0].toLowerCase();
                String[] commandArgs = Arrays.copyOfRange(commandParts, 1, commandParts.length);

                // Execute command with input from the previous command's output
                intermediateOutput = executeCommand(command, commandArgs, intermediateOutput);
            }

            // Print the final output
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

//public class PipeCommand implements Command {
//    @Override
//    public void execute(String[] args) {
//        if (args.length < 2) {
//            System.out.println("Invalid pipe command. Usage: command1 | command2");
//            return;
//        }
//
//        // Split the arguments into the first and second commands
//        String firstCommandName = args[0].trim();
//        String secondCommandName = args[1].trim();
//
//        // Execute the first command
//        String output = executeCommand(firstCommandName, args);
//        if (output != null) {
//            // Pass the output to the second command
//            executeSecondCommand(secondCommandName, output);
//        }
//    }
//
//    private String executeCommand(String commandName, String[] args) {
//        StringBuilder output = new StringBuilder();
//        try {
//            // Set up the command process
//            ProcessBuilder processBuilder = new ProcessBuilder(commandName);
//            String[] commandWithArgs = new String[args.length + 1];
//            commandWithArgs[0] = commandName;
//            System.arraycopy(args, 0, commandWithArgs, 1, args.length);
//            processBuilder.command(commandWithArgs);
//            Process process = processBuilder.start();
//
//            // Capture the output of the command
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    output.append(line).append(System.lineSeparator());
//                }
//            }
//
//            process.waitFor(); // Wait for the process to complete
//            return output.toString(); // Return captured output
//        } catch (IOException | InterruptedException e) {
//            System.out.println("Error executing command: " + commandName);
//            return null;
//        }
//    }
//
//    private void executeSecondCommand(String commandName, String input) {
//        switch (commandName.toLowerCase()) {
//            case "uniq":
//                executeUniq(input);
//                break;
//            case "sort":
//                executeSort(input);
//                break;
//            case "grep":
//                executeGrep(input);
//                break;
//            // Add other commands here as needed
//            default:
//                System.out.println("Invalid command: " + commandName);
//        }
//    }
//
//    private void executeUniq(String input) {
//        LinkedHashSet<String> uniqueLines = new LinkedHashSet<>();
//        for (String line : input.split(System.lineSeparator())) {
//            uniqueLines.add(line);
//        }
//        uniqueLines.forEach(System.out::println);
//    }
//
//    private void executeSort(String input) {
//        String[] lines = input.split(System.lineSeparator());
//        Arrays.sort(lines);
//        for (String line : lines) {
//            System.out.println(line);
//        }
//    }
//
//    private void executeGrep(String input) {
//        // Example grep implementation that filters based on a hardcoded term
//        String searchTerm = "3"; // You can modify this to get from args or input
//        for (String line : input.split(System.lineSeparator())) {
//            if (line.contains(searchTerm)) {
//                System.out.println(line);
//            }
//        }
//    }
//}
/*
 * @Override
 * public void execute(String[] args) {
 * // Split the commands by pipe symbol
 * String[] commands = args[0].split("\\|");
 * String input = null;
 * 
 * for (String commandStr : commands) {
 * // Trim whitespace and split the command and its arguments
 * String commandPart = commandStr.trim();
 * String[] commandArgs = commandPart.split("\\s+");
 * 
 * // Get the command from the CommandFactory
 * Command command = CommandFactory.getCommand(commandArgs[0]);
 * 
 * if (command != null) {
 * // Capture the output of the command
 * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 * PrintStream originalOut = System.out;
 * System.setOut(new PrintStream(outputStream));
 * 
 * // Execute the command
 * command.execute(Arrays.copyOfRange(commandArgs, 1, commandArgs.length));
 * 
 * // Restore the original output
 * System.setOut(originalOut);
 * 
 * // Get the output and prepare it as input for the next command
 * input = outputStream.toString().trim();
 * } else {
 * System.out.println("Invalid command in pipe: " + commandArgs[0]);
 * return; // Exit on invalid command
 * }
 * }
 * 
 * // Output the final result of the piped commands
 * System.out.println(input);
 * }
 * }
 */

/*
 * public class PipeCommand implements Command {
 *
 * @Override
 * public void execute(String[] args) {
 * if (args.length < 3 || !args[1].equals("|")) {
 * System.out.println("pipe requires two commands separated by |.");
 * return;
 * }
 *
 * String[] firstCommandArgs = args[0].split(" ");
 * String[] secondCommandArgs = args[2].split(" ");
 *
 * Command firstCommand = CommandFactory.getCommand(firstCommandArgs[0]);
 * Command secondCommand = CommandFactory.getCommand(secondCommandArgs[0]);
 *
 * if (firstCommand == null || secondCommand == null) {
 * System.out.println("Invalid command in pipe.");
 * return;
 * }
 *
 * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 * PrintStream originalOut = System.out;
 * System.setOut(new PrintStream(outputStream));
 *
 * try {
 * firstCommand.execute(firstCommandArgs);
 * System.out.flush();
 * } finally {
 * System.setOut(originalOut);
 * }
 *
 * String firstCommandOutput = outputStream.toString();
 * secondCommandArgs = new String[] {secondCommandArgs[0], firstCommandOutput};
 *
 * secondCommand.execute(secondCommandArgs);
 * }
 * }
 */
