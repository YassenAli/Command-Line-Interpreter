package org.cmd.commands;

public class CommandFactory {
    public static Command getCommand(String commandName) {
        switch (commandName.toLowerCase()) {
            case "cat":
                return new CatCommand();
            case ">":
                return new RedirectCommand();
            case ">>":
                return new AppendCommand();
            case "|":
                return new PipeCommand();
            case "pwd":
                return new PwdCommand();
            case "cd":
                return new CdCommand();
            case "exit":
                return new ExitCommand();
            case "help":
                return new HelpCommand();
            case "ls":
                return new ListCommand();
            case "mkdir":
                return new MkdirCommand();
            // Add cases for other commands here
            case "touch":
                return new TouchCommand();
            case "rm" :
                return  new RemoveCommand();
            case "rmdir" :
                return new RemoveDirectoryCommand();
            default:
                return null;
        }
    }
}