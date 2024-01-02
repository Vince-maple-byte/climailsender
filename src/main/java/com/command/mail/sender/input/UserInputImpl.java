package com.command.mail.sender.input;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserInputImpl implements UserInput {

    //FINISHED: For safe practices we need to use the Address class where we can verify the email is valid
    @Override
    public String email(Scanner input) throws AddressException{
        try{
            System.out.println("Enter an email:");
            String result = input.nextLine();
            InternetAddress email = new InternetAddress(result);
            email.validate();

            return email.getAddress();
        } catch (AddressException e){
            throw new AddressException("Email is invalid");
        }
    }
    @Override
    public String subject(Scanner input) {
        System.out.println("Enter the Subject: ");
        return input.nextLine();
    }

    @Override
    public String textBody(Scanner input) {
        System.out.println("Enter the body that you want: ");
        String textBody = input.nextLine();
        if(textBody.endsWith(".txt") || textBody.substring(0, textBody.length()-1).endsWith(".txt")) return fileToTextBody(textBody);
        else return textBody;
    }

    public String fileToTextBody(String pathName){
        if(pathName.endsWith("\""))pathName = pathName.substring(1, pathName.length()-1);
        StringBuilder text = new StringBuilder();
        File file = new File(pathName);
        if(!file.exists()) return "File: " + pathName + " does not exist";
        if(!file.canRead()) return "File: " + pathName + " does not have reading permission.";

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String input = "";
            while((input = bufferedReader.readLine()) != null){
                text.append(input);
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return text.toString();
    }

    @Override
    //Just need to add the simplified name of the file instead of full pathname DON'T NEED TO DO THIS
    //This method allows for us
    public ArrayList<String> attachments(Scanner input) {
        System.out.println("Type in the file path of files to attach\n" +
                "Type q or quit when you don't want to enter anymore files.");
        ArrayList<String> fileInput = new ArrayList<>(); // This is where we are going to store all of the files in each of the
        while(true){
            String file = input.next();
            if(file.equalsIgnoreCase("q") || file.equalsIgnoreCase("quit")) break; // Leaves the loop so that we know
            //This is used to remove the "" that surround the file path when someone copies it from the file explorer to the command line
            if(file.charAt(0) == '"') file = file.substring(1);
            if(file.charAt(file.length()-1) == '"') file = file.substring(0, file.length()-1);
            fileInput.add(file);
        }

        /*We are doing the below in order to fix the issue of when we have spaces in the middle of the file path
        and when we save the input to the array fileInput it would have each element be separated by a space instead
         of having each element of the array being a file path.
        Example of the this issue {"C:\Users\BunnySoo\Documents\Computer", "Science", "books\School\compnetworks.pdf",}
        Instead of {C:\Users\BunnySoo\Documents\Computer Science books\School\compnetworks.pdf}*/
        ArrayList<String> fileList = new ArrayList<>();
        if(fileInput.size() > 1){
            StringBuilder currentFile = new StringBuilder(fileInput.get(0) + " ");

            for(int i = 1; i < fileInput.size(); i++){
                //Checks if the current element starts with C:\ and if it does we add the contents of the currentFile to the array
                //and we set the currentFile to the string at element i of the array
                if(fileInput.get(i).length() > 3 && fileInput.get(i).startsWith("C:\\")){
                    fileList.add(currentFile.toString());
                    currentFile = new StringBuilder(fileInput.get(i)+ " ");
                }
                else if(i == fileInput.size() - 1){
                    currentFile.append(fileInput.get(i));
                    fileList.add(currentFile.toString());
                }
                else {
                    currentFile.append(fileInput.get(i)).append(" ");
                }
            }
        }
        else {
            fileList.add(fileInput.get(0));
        }

        for(int i = 0; i < fileList.size(); i++){
            if(fileList.get(i).endsWith(" ")){
                fileList.set(i, fileList.get(i).substring(0, fileList.get(i).length() - 1));
            }
        }

        return fileList;
    }

    /*
    * This method for getting the all the files that are going embedded in the html file
    * The implementation of this method is almost identical to the attachments method with the added
    * exception of having the amountOfContentIDs being a paramater allowing for the loop to have a certain set of iterations.
    *
    * The user needs to make sure that for every file typed they have to press enter.
    * */
    @Override
    public ArrayList<String> inlineElements(Scanner input, int amountOfContentIDs) {
        System.out.println("Type in the file path of files you want to be embedded in your html email\n" +
                "Make sure to press enter after each file that you want to enter as the full path name, " +
                "and have it in the same order as the content-id's that you want them to be associated with.");
        ArrayList<String> fileInput = new ArrayList<>(); // This is where we are going to store all of the files in each of the
        for(int i = 0; i < amountOfContentIDs; i++){
            String file = input.nextLine();
            //This is used to remove the "" that surround the file path when someone copies it from the file explorer to the command line
            if(file.charAt(0) == '"') file = file.substring(1);
            if(file.charAt(file.length()-1) == '"') file = file.substring(0, file.length()-1);
            if(file.charAt(file.length()-1) == ' ') file = file.substring(0, file.length() - 2);
            if(file.startsWith(" \"")) file = file.substring(2);
            fileInput.add(file);
        }

        return fileInput;
    }

    @Override
    public String[] getContentIdFromHtml(String htmlText) {
        String regex = "\"cid:(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlText);


        List<String> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(matcher.group(1));
        }
        return matches.toArray(new String[0]);
    }


}

