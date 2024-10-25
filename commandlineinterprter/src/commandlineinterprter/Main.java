package commandlineinterprter;

import commandlineinterprter.commands.Command;
import commandlineinterprter.commands.CommandFactory;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

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

//public class Main {
//    public static String currentDirectory = System.getProperty("user.dir");
//    public static final String homeDirectory = System.getProperty("user.dir");
//
//    public static void main(String[] args) throws IOException {
//        Scanner in = new Scanner(System.in);
//
//        while (true) {
//            Terminal terminal = new Terminal();
//            terminal.pwd();
//            System.out.print(":-$ ");
//            String command = in.nextLine();
//            System.out.println();
//
//            boolean pipe = true;
//            int commandNumber = 0;
//            int lastSlash = 0;
//            String nextCommand = "";
//
//            while (pipe && commandNumber < 2) {
//                commandNumber++;
//
//                /*lastSlash = command.indexOf("|");
//
//                if (lastSlash != -1 && commandNumber == 1) {
//                    nextCommand = command.substring(lastSlash + 1);
//                    command = command.substring(0, lastSlash);
//                } else {
//                    pipe = false;
//                }*/
//
//                String[] splited = command.split("\\s+");
//                Parser parse = new Parser(splited);
//                String cmd = parse.getCmd();
//
//                if (cmd.equalsIgnoreCase("help")) {
//                    if (parse.getFirstArg().equals("")) {
//                        terminal.help();
//                    } else {
//                        System.out.println("help doesn't take any parameters");
//                    }
//                } else if (cmd.equalsIgnoreCase("exit")) {
//                    if (parse.getFirstArg().equals("")) {
//                        System.out.println("You have exited.");
//                        System.exit(0);
//                    } else {
//                        System.out.println("exit doesn't take any parameter");
//                    }
//                } else if (cmd.equalsIgnoreCase("pwd")) {
//                    if (parse.getFirstArg().equals("")) {
//                        System.out.print("Your current directory is: ");
//                        terminal.pwd();
//                        System.out.println();
//                    } else {
//                        System.out.println("pwd doesn't take any argument");
//                    }
//                } else if (cmd.equalsIgnoreCase("cd")) {
//                    if (parse.getSecondArg().equals("")) {
//                        terminal.cd(parse.getFirstArg(), Main.currentDirectory);
//                    } else {
//                        System.out.println("cd takes only one parameter");
//                    }
//                } else {
//                    System.out.println("Invalid command");
//                }
//            }
//        }
//    }
//}
