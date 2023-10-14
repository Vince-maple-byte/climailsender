package com.command.mail.sender.input;
/* We create the methods for the user to give the email that they are sending it to,
        The text body,
        the subject,
        adding inline elements,
        and adding typical mail attachments such as .pdf, .txt, etc.*/
import java.util.Scanner;
public interface UserInput {

    public String subject(Scanner input);
    public String email(Scanner input);
    public String textBody(Scanner input);
    //Going to need to have two String elements to add attachments, filename and location of file
    public String[] attachments(Scanner input);
    public String[] inlineElements(Scanner input);
}
