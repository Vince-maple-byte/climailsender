package com.command.mail.sender.commands;

import com.command.mail.sender.input.UserInputImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SendMailImplTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private UserInputImpl userInput;
    private SendMailImpl sendMail;

    @BeforeEach
    void setUp() throws MessagingException {
        sendMail = new SendMailImpl(javaMailSender, userInput);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createMimeMessageHelper() throws MessagingException, IOException {

    }

    @Test
    void sendMessage() {
    }
}