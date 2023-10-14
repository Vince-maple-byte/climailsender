package com.command.mail.sender.input;

import java.util.Scanner;

public class UserInputImpl implements UserInput {

    @Override
    public String email(Scanner input) {
        System.out.println("Enter an email to read");
        return input.nextLine();
    }
    @Override
    public String subject(Scanner input) {

        return null;
    }

    @Override
    public String textBody(Scanner input) {
        return null;
    }

    @Override
    public String[] attachments(Scanner input) {
        return new String[0];
    }

    @Override
    public String[] inlineElements(Scanner input) {
        return new String[0];
    }
}

