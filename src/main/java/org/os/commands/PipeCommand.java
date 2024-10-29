package org.os.commands;

import java.io.*;
import java.util.Arrays;

public class PipeCommand implements Command {
    @Override
    public void execute(String[] args) {
        // Split the commands by pipe symbol
        String[] commands = args[0].split("\\|");
        String input = null;

        for (String commandStr : commands) {
            // Trim whitespace and split the command and its arguments
            String commandPart = commandStr.trim();
            String[] commandArgs = commandPart.split("\\s+");

            // Get the command from the CommandFactory
            Command command = CommandFactory.getCommand(commandArgs[0]);

            if (command != null) {
                // Capture the output of the command
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream originalOut = System.out;
                System.setOut(new PrintStream(outputStream));

                // Execute the command
                command.execute(Arrays.copyOfRange(commandArgs, 1, commandArgs.length));

                // Restore the original output
                System.setOut(originalOut);

                // Get the output and prepare it as input for the next command
                input = outputStream.toString().trim();
            } else {
                System.out.println("Invalid command in pipe: " + commandArgs[0]);
                return; // Exit on invalid command
            }
        }

        // Output the final result of the piped commands
        System.out.println(input);
    }
}

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
