package org.os;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.os.commands.Command;
import org.os.commands.CommandFactory;

public class Main {
    public static String currentDirectory = System.getProperty("user.dir");
    public static final String homeDirectory = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Command Line Interpreter. Type 'help' for a list of commands.");

        while (true) {
            System.out.print(currentDirectory + ":-$ ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty())
                continue;
            if (input.equals("exit"))
                break;

            String[] splitInput = input.split("\\s+");
            String commandName = splitInput[0];

            if (input.contains(">") || input.contains(">>")) {
                handleRedirection(input);
            } else if (input.contains("|")) {
                handlePipe(input);
            } else {
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

    public static void handleRedirection(String input) {
        String[] splitInput = input.split("\\s+");
        boolean append = input.contains(">>");

        int redirectIndex = input.indexOf(append ? ">>" : ">");
        String commandPart = input.substring(0, redirectIndex).trim();
        String filename = input.substring(redirectIndex + (append ? 2 : 1)).trim();

        String[] commandArgs = commandPart.split("\\s+");

        Command command = append ? CommandFactory.getCommand(">>") : CommandFactory.getCommand(">");

        if (command != null) {
            String[] fullCommandArgs = Arrays.copyOf(commandArgs, commandArgs.length + 1);
            fullCommandArgs[fullCommandArgs.length - 1] = filename;

            command.execute(fullCommandArgs);
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