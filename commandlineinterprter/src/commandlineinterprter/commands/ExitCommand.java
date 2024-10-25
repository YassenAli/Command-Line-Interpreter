package commandlineinterprter.commands;

public class ExitCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println("You have exited.");
        System.exit(0);
    }
}
