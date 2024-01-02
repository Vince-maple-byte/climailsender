package com.command.mail.sender.commands;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface SendMail {

    public void simpleMessage(@ShellOption(value = {"-m", "--message"},
            defaultValue = "")String test) throws MessagingException;
    public void sendAttachments(
            @ShellOption(value = {"-t", "--textFile"}, defaultValue = "") String fileInputLocation
    ) throws MessagingException, FileNotFoundException;

    public void sendHtml(@ShellOption(value = {"-h", "--html"}, defaultValue = "") String htmlText,
                         @ShellOption(value = {"-a", "--attach"}, defaultValue = "false") boolean attach
    ) throws MessagingException, IOException;
}
