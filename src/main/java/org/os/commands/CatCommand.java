package org.os.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CatCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: cat <filename>");
            return;
        }

        String filename = args[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("File not found or error reading file: " + e);
        }
    }
}

//    public void execute(String filePath) {
//        try (BufferedReader reader = new BufferedReader((new FileReader(filePath)))){
//            String line;
//            while((line = reader.readLine()) != null){
//                System.out.println(line);
//            }
//        } catch (IOException e){
//            System.out.println("Error reading file: "+e.getMessage());
//        }
//    }
