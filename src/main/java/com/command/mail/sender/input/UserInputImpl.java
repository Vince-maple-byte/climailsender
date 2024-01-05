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
    //This method allows for us to get user input of the files that they either want to attach or have included in the html file
    public ArrayList<String> inputFileNames(Scanner input, boolean inline) {
        if(inline){
            System.out.println("""
                    Type in the file path of files you want to be embedded in your html email
                    Make sure to press enter after each file that you want to enter as the full path name,\s
                    and have it in the same order as the content-id's that you want them to be associated with.
                    Type q or quit when you don't want to enter anymore files at the last line.""");
        }
        else {
            System.out.println("Type in the file path of files to attach in separate lines\n" +
                    "Type q or quit when you don't want to enter anymore files at the last line.");
        }

        ArrayList<String> fileInput = new ArrayList<>(); // This is where we are going to store all of the files in each of the
        while(true){
            String file = input.nextLine();
            if(file.equalsIgnoreCase("q") || file.equalsIgnoreCase("quit")) break; // Leaves the loop so that we know
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

