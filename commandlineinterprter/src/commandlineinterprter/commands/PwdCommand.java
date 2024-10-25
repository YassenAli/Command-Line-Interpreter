package commandlineinterprter.commands;

import commandlineinterprter.Main;

public class PwdCommand implements Command {
    @Override
    public void execute(String[] args){
        System.out.print(Main.currentDirectory);
    }
}

//public void pwd() {
//
//}

