package com.command.mail.sender.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public interface FileCreation {

    public ArrayList<File> createFileFromString(ArrayList<String> filePath) throws FileNotFoundException;
    public File createHtmlFile(Scanner input);
    public String convertHtmlFileIntoAString(File html) throws IOException;
}
