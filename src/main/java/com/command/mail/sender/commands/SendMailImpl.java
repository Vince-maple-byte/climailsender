package com.command.mail.sender.commands;

import com.command.mail.sender.input.FileCreationImpl;
import com.command.mail.sender.input.UserInputImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

@ShellComponent
public class SendMailImpl implements SendMail{

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserInputImpl userInput;
    FileCreationImpl fileCreation = new FileCreationImpl();

    public SendMailImpl(JavaMailSender javaMailSender, UserInputImpl userInput) {
    }

    @Override
    @ShellMethod(key = "simpleMessage", value="This command sends an simple email to the specified " +
            "email address.\n You cannot include attachments with this command, " +
            "to do this use sendAttachments.")
    public void simpleMessage(
            @ShellOption(value = {"-f", "--fileInput"}, defaultValue = "")
            String fileInputLocation) throws MessagingException {
        if(!fileInputLocation.isEmpty()){
            //Where the text body of the email is going to be set with the contents of a text file
            System.out.println("Options work perfectly!!!! " + fileInputLocation);
            return;
        }
        Scanner scanner = new Scanner(System.in);
        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(send, true);

        message.setTo(userInput.email(scanner));
        message.setSubject(userInput.subject(scanner));
        message.setText(userInput.textBody(scanner), false);

        javaMailSender.send(send);
    }

    @Override
    @ShellMethod(key = "sendAttachments", value="This command sends an email to the specified email address" +
            "with attachments included")
    public void sendAttachments() throws MessagingException, FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(send, true);

        message.setTo(userInput.email(scanner));
        message.setSubject(userInput.subject(scanner));
        message.setText(userInput.textBody(scanner), false);

        System.out.println("When entering file attachments/inline elements, " +
                "we can not guarantee if the files would go through do to email server limitations");

        ArrayList<String> attachmentString = userInput.attachments(scanner);
        ArrayList<File> attachmentList = fileCreation.createFileFromString(attachmentString);
        for (File file : attachmentList) {
            message.addAttachment(file.getName(), file);
        }

        javaMailSender.send(send);
    }

    @Override
    @ShellMethod(key = "sendHtml", value="This command sends an html emails with images, videos, etc" +
            " being embedded into it. ")
    public void sendHtml(
            @ShellOption(value = {"-f", "--htmlFile"}, defaultValue = "") String htmlFileLocation
    ) throws MessagingException, IOException {
        Scanner scanner = new Scanner(System.in);
        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(send, true);

        message.setTo(userInput.email(scanner));
        message.setSubject(userInput.subject(scanner));

        /*What is going to be done here:
         * Get the html file path and Make a file object createHtmlFile()
         * Convert the contents of the html file object into a String convertHtmlFileIntoAString()
         *
         * get the content-id from the html String getContentIdFromHtml()
         * get the inline element file inlineElements()
         *
         * use a while loop to add each inline elements message.addInline()
         *
         */

        String[] contentId;
        ArrayList<String> inlineElements;
        File htmlFile;
        if(htmlFileLocation.isEmpty()) htmlFile = fileCreation.createHtmlFile(scanner);
        else htmlFile = new File(htmlFileLocation);
        String htmlText = fileCreation.convertHtmlFileIntoAString(htmlFile);
        contentId = userInput.getContentIdFromHtml(htmlText);
        inlineElements = userInput.inlineElements(scanner, contentId.length);
        ArrayList<File> inlineFiles = fileCreation.createFileFromString(inlineElements);
        message.setText(htmlText, true);

        for(int i = 0; i < contentId.length; i++){
            if(i >= inlineFiles.size()){
                break;
            }
            else {
                message.addInline(contentId[i], inlineFiles.get(i));
            }
        }


        javaMailSender.send(send);
    }
}
