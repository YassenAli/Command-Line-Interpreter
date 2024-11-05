package org.os.commands;

import org.os.Main;

public class PwdCommand implements Command {
    @Override
    public void execute(String[] args){
        System.out.println(Main.currentDirectory);
    }
}
