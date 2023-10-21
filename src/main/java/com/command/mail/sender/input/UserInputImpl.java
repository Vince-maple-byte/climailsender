package com.command.mail.sender.input;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserInputImpl implements UserInput {

    //For safe practices we need to use the Address class where we can verify the email is valid
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

    //Im going to make another method for file input (might do this in another class)
    //In which we have the user be able to give a .txt and submit it as the text body
    @Override
    public String textBody(Scanner input) {
        System.out.println("Enter the body that you want: ");
        return input.nextLine();
    }

    /*
    * Need to use the URI class to validate and get the class path of the attachments
    * that they give me.
    * Might even have the option of the user being able to write all of the attachments that they want
    * in a .txt file
    * */
    @Override
    //Just need to add the simplified name of the file instead of full pathname
    public String[] attachments(Scanner input) {
        System.out.println("Type in the file path of files to attach\n" +
                "Make sure to have all of the files in the same line separated by a space");
        String files = input.nextLine();
        String[] fileList = files.split("\\sC:"); //This would return the file without the C:
        for(int i = 1; i < fileList.length; i++){
            fileList[i] = "C:" + fileList[i]; //In here we add the C: at the beginning of the string giving it the absolute name of the string
        }
        //This is just a small change so that if a space was added at the end but their is no more files being added we can remove the space
        //Most likely won't make much of a change for when we are adding the files but it would give me piece of mind
        char[] checkSpace = fileList[fileList.length-1].toCharArray();
        if(checkSpace[checkSpace.length-1] == ' '){
            fileList[fileList.length-1] = fileList[fileList.length-1].substring(0, checkSpace.length-1);
        }
        return fileList;
    }

    /*
    * This are for things that can be displayed on the email itself such as images, */
    @Override
    public String[] inlineElements(Scanner input) {
        return new String[0];
    }
}

