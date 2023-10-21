package com.command.mail.sender.commands;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

public interface SendMail {

    public MimeMessageHelper createMimeMessageHelper(MimeMessage send);
    public void sendMessage();
}
