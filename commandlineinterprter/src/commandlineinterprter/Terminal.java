package commandlineinterprter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Terminal {
	public void pwd() {
		System.out.print(Main.currentDirectory);
	}
	public void help() {
		System.out.println("pwd: print the path of the current directory");
		System.out.println("cd: change directory");
		System.out.println("ls: view content of directory");
		System.out.println("ls -a: List all files, including hidden");
		System.out.println("ls -r: List files recursively");
		System.out.println("mkdir: Create a directory");
		System.out.println("rmdir: Remove a directory");
		System.out.println("touch: Create an empty file");
		System.out.println("mv: Move or rename a file");
		System.out.println("rm: Remove a file");
		System.out.println("cat: Display file content");
		System.out.println(">: Redirect output to a file");
		System.out.println(">>: Append output to a file");
		System.out.println("|: Pipe output from one command to another");
	}
	public void cd(String args,String adress) {
		if (args.equalsIgnoreCase("~")) {
			Main.currentDirectory = Main.homeDirectory;
		}
		else if(args.equalsIgnoreCase("-")) {
			try {
				int lastSlash = adress.lastIndexOf("\\");
				String adressParent = adress.substring(0, lastSlash);
				Main.currentDirectory = adressParent;
			}catch(Exception e) {
				System.out.print("you are actually at the root");
			}
		}
		else if (args.equalsIgnoreCase("/")) {
			try {
				int lastSlash = adress.indexOf("\\");
				String root = adress.substring(0,lastSlash);
				Main.currentDirectory = root;
			}
			catch(Exception e) {
				System.out.print("you are at the root");
			}
		}
		else if(args.equalsIgnoreCase("~")) {
			Main.currentDirectory = Main.homeDirectory;
		}
		else {
			System.out.println("cd command can take one of these arguments only -,/,,~");
		}
	}
}
