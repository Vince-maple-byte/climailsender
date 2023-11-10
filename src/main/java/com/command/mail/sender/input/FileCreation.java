package com.command.mail.sender.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface FileCreation {

    public ArrayList<File> createFileFromString(ArrayList<String> filePath) throws FileNotFoundException;
}
