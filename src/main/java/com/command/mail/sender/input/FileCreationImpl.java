package com.command.mail.sender.input;

import java.io.File;
import java.io.FileNotFoundException;

public class FileCreationImpl implements FileCreation{
    @Override
    public File[] createFileFromString(String[] filePath) throws FileNotFoundException {
        File[] fileList = new File[filePath.length];

        for(int i = 0; i < filePath.length; i++){
            fileList[i] = new File(filePath[i]);
            if(!(fileList[i].exists())){
                throw new FileNotFoundException("File does not exist " + filePath[i]);
            }
        }

        return fileList;
    }
}
