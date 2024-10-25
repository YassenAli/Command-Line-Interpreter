package commandlineinterprter;

import commandlineinterprter.commands.Command;
import commandlineinterprter.commands.CommandFactory;
import java.io.IOException;
import java.util.Scanner;

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

            String[] splitInput = input.split("\\s+");
            String commandName = splitInput[0];
            String[] commandArgs = new String[splitInput.length - 1];
            System.arraycopy(splitInput, 1, commandArgs, 0, commandArgs.length);

            Command command = CommandFactory.getCommand(commandName);

            if (command != null) {
                command.execute(commandArgs);
            } else {
                System.out.println("Invalid command: " + commandName);
                System.out.println("Type 'help' for a list of valid commands.");
            }
        }
    }
}
