package com.command.mail.sender.input;
/* We create the methods for the user to give the email that they are sending it to,
        The text body,
        the subject,
        adding inline elements,
        and adding typical mail attachments such as .pdf, .txt, etc.*/
import jakarta.mail.internet.AddressException;

import java.util.ArrayList;
import java.util.Scanner;
public interface UserInput {

    public String subject(Scanner input);
    public String email(Scanner input) throws AddressException;
    public String textBody(Scanner input);

    //Going to need to have two String elements to add attachments, filename and location of file
    public ArrayList<String> inputFileNames(Scanner input, boolean inline);


    public String[] getContentIdFromHtml(String html);

}
