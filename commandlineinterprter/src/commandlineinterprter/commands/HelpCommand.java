package commandlineinterprter.commands;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println("pwd: print the path of the current directory");
        System.out.println("cd: change directory");
        System.out.println("ls: view content of directory");
        System.out.println("ls -a: List all files, including hidden");
        System.out.println("ls -r: List files recursively");
        System.out.println("mkdir: Create a directory");
        System.out.println("rmdir: Remove a directory");
        System.out.println("touch: Create an empty file");
        System.out.println("mv: Move or rename a file");
        System.out.println("rm: Remove a file");
        System.out.println("cat: Display file content");
        System.out.println(">: Redirect output to a file");
        System.out.println(">>: Append output to a file");
        System.out.println("|: Pipe output from one command to another");
    }
}

