package org.cmd.commands;

import org.cmd.Main;

public class PwdCommand implements Command {
    @Override
    public void execute(String[] args){
        System.out.print(Main.currentDirectory);
    }
}
