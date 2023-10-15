package com.command.mail.sender.input;

import java.util.Scanner;

public class UserInputImpl implements UserInput {

    //For safe practices we need to use the Address class where we can verify the email is valid
    @Override
    public String email(Scanner input) {
        System.out.println("Enter an email");
        return input.nextLine();
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
    public String[] attachments(Scanner input) {
        return new String[0];
    }

    /*
    * This are for things that can be displayed on the email itself such as images, */
    @Override
    public String[] inlineElements(Scanner input) {
        return new String[0];
    }
}

