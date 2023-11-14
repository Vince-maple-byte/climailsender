package com.command.mail.sender.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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

    //TODO need to make a method for handling the inline element files by converting them string to a file


}
