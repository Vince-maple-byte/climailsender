package com.command.mail.sender.input;

import java.io.File;
import java.io.FileNotFoundException;

public interface FileCreation {

    public File[] createFileFromString(String[] filePath) throws FileNotFoundException;
}
