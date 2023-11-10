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
    //Just need to add the simplified name of the file instead of full pathname
    public MimeMessageHelper createMimeMessageHelper(MimeMessage send) {
        try{
            Scanner scanner = new Scanner(System.in);
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
            

            return message;
        }
        catch(MessagingException | FileNotFoundException e ){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @ShellMethod(key = "sendEmail", value="This command sends an email to the specified email address")
    public void sendMessage(){

        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = createMimeMessageHelper(send);

        javaMailSender.send(send);
    }
}
