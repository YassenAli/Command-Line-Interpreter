package org.os.commands;

import java.io.*;

public class PipeCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: command1 | command2");
            return;
        }

        // Split commands into two parts
        String[] command1 = args[0].trim().split("\\s+");
        String[] command2 = args[1].trim().split("\\s+");

        // Adjust command names for Windows if needed
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            if (command1[0].equals("ls")) {
                command1[0] = "cmd.exe";
                command1 = new String[]{"cmd.exe", "/c", "dir"};
            }
            if (command2[0].equals("grep")) {
                command2[0] = "findstr";
            }
        }

        try {
            // Set up ProcessBuilder for the first command
            ProcessBuilder processBuilder1 = new ProcessBuilder(command1);
            Process process1 = processBuilder1.start();

            // Set up ProcessBuilder for the second command
            ProcessBuilder processBuilder2 = new ProcessBuilder(command2);
            Process process2 = processBuilder2.start();

            // Pipe output from process1 to process2
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process1.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process2.getOutputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                    writer.flush(); // Ensure each line is sent immediately
                }
            }

            // Wait for both processes to complete
            int exitCode1 = process1.waitFor();
            int exitCode2 = process2.waitFor();

            // Output exit statuses or errors for each process
            if (exitCode1 == 0 && exitCode2 == 0) {
                System.out.println("Piping executed successfully.");
            } else {
                System.err.println("Error: First command exited with code " + exitCode1);
                System.err.println("Error: Second command exited with code " + exitCode2);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error during pipe execution: " + e.getMessage());
        }
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
