package commandlineinterprter.commands;

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
            // Add cases for other commands here
            default:
                return null;
        }
    }
}