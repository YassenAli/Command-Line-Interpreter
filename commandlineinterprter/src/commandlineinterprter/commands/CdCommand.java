package commandlineinterprter.commands;

import commandlineinterprter.Main;

public class CdCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("cd requires an argument.");
            return;
        }

        String argument = args[1];
        String currentAddress = Main.currentDirectory;

        cd(argument, currentAddress);
    }

    private void cd(String argument, String address) {
        if (argument.equalsIgnoreCase("~")) {
            Main.currentDirectory = Main.homeDirectory;
        } else if (argument.equalsIgnoreCase("-")) {
            try {
                int lastSlash = address.lastIndexOf("\\");
                if (lastSlash > 0) {
                    String parentAddress = address.substring(0, lastSlash);
                    Main.currentDirectory = parentAddress;
                } else {
                    System.out.println("You are already at the root.");
                }
            } catch (Exception e) {
                System.out.println("Error accessing the parent directory.");
            }
        } else if (argument.equalsIgnoreCase("/")) {
            try {
                int rootSlash = address.indexOf("\\");
                if (rootSlash > 0) {
                    String rootAddress = address.substring(0, rootSlash);
                    Main.currentDirectory = rootAddress;
                } else {
                    System.out.println("You are at the root.");
                }
            } catch (Exception e) {
                System.out.println("Error accessing the root directory.");
            }
        } else {
            System.out.println("cd command can take one of these arguments only: -, /, or ~");
        }
    }
}