package com.command.mail.sender.commands;

import com.command.mail.sender.input.UserInputImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Scanner;

@ShellComponent
public class SendMailImpl implements SendMail{

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserInputImpl userInput;

    @Override
    public MimeMessageHelper createMimeMessageHelper(MimeMessage send) {
        try{
            Scanner scanner = new Scanner(System.in);
            MimeMessageHelper message = new MimeMessageHelper(send, true);

            message.setTo(userInput.email(scanner));
            message.setSubject(userInput.subject(scanner));
            message.setText(userInput.textBody(scanner));

            return message;
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @ShellMethod(key = "sendEmail", value="This command sends an email to the specified email address")
    public void sendMessage() {

        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = createMimeMessageHelper(send);

        javaMailSender.send(send);
        System.out.println("Successful");
    }
}
