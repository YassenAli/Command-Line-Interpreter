package org.os;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import org.os.commands.*;

public class Main {
    public static String currentDirectory = System.getProperty("user.dir");
    public static final String homeDirectory = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Command Line Interpreter. Type 'help' for a list of commands.");

        while (true) {
            System.out.print(currentDirectory + ":-$ ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;
            if (input.equals("exit")) break;

            String[] splitInput = input.split("\\s+");
            String commandName = splitInput[0];

            // Handle redirection and piping cases
            if (input.contains(">") || input.contains(">>")) {
                handleRedirection(input);
            } else if (input.contains("|")) {
                handlePipe(input);
            } else {
                // Normal command execution
                Command command = CommandFactory.getCommand(commandName);
                if (command != null) {
                    String[] commandArgs = Arrays.copyOfRange(splitInput, 1, splitInput.length);
                    command.execute(commandArgs);
                } else {
                    System.out.println("Invalid command: " + commandName);
                }
            }
        }
    }

    private static void handleRedirection(String input) {
        String[] splitInput = input.split("\\s+");
        String commandOutput = splitInput[0];
        boolean append = input.contains(">>");
        Command command = append ? CommandFactory.getCommand(">>") : CommandFactory.getCommand(">");

        if (command != null) {
            command.execute(Arrays.copyOfRange(splitInput, 1, splitInput.length));
        } else {
            System.out.println("Invalid redirection command.");
        }
    }

    private static void handlePipe(String input) {
        String[] pipeParts = input.split("\\|");
        Command command = CommandFactory.getCommand("|");
        if (command != null) {
            command.execute(pipeParts);
        } else {
            System.out.println("Invalid pipe command.");
        }
    }
}