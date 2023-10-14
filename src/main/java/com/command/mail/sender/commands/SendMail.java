package com.command.mail.sender.commands;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.shell.standard.ShellOption;

public interface SendMail {

    public MimeMessageHelper createMimeMessageHelper(MimeMessage send);
    public void sendMessage();
}
