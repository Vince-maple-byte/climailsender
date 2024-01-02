package com.command.mail.sender.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class FileCreationImpl implements FileCreation{
    @Override
    public ArrayList<File> createFileFromString(ArrayList<String> filePath) throws FileNotFoundException {
        ArrayList<File> fileList = new ArrayList<>();

        for (String s : filePath) {
            File file = new File(s);
            if (!file.exists()) {
                System.out.println("File: " + s + " does not exist.");
            } else {
                fileList.add(file);
            }
        }

        return fileList;
    }

    //Is going to create the File object from the html file path
    @Override
    public File createHtmlFile(Scanner input) {
        System.out.println("Type in the file path of the .html file");
        String file = input.nextLine();
        if(file.charAt(0) == '"') file = file.substring(1, file.length()-1);
        return new File(file);
    }

    //Converts the File object contents into a String
    @Override
    public String convertHtmlFileIntoAString(File html) throws IOException {
        byte[] encoded = Files.readAllBytes(html.toPath());
        return new String(encoded, StandardCharsets.UTF_8);
    }




}
