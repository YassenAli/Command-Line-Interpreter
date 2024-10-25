package commandlineinterprter.commands;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PipeCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 3 || !args[1].equals("|")) {
            System.out.println("pipe requires two commands separated by |.");
            return;
        }

        String[] firstCommandArgs = args[0].split(" ");
        String[] secondCommandArgs = args[2].split(" ");

        Command firstCommand = CommandFactory.getCommand(firstCommandArgs[0]);
        Command secondCommand = CommandFactory.getCommand(secondCommandArgs[0]);

        if (firstCommand == null || secondCommand == null) {
            System.out.println("Invalid command in pipe.");
            return;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            firstCommand.execute(firstCommandArgs);
            System.out.flush();
        } finally {
            System.setOut(originalOut);
        }

        String firstCommandOutput = outputStream.toString();
        secondCommandArgs = new String[] {secondCommandArgs[0], firstCommandOutput};

        secondCommand.execute(secondCommandArgs);
    }
}
