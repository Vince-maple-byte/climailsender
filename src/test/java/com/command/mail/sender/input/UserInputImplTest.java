package com.command.mail.sender.input;

import jakarta.mail.internet.AddressException;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;

class UserInputImplTest {
    private UserInputImpl userInput = new UserInputImpl();
    private final InputStream systemIn = System.in;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        System.setIn(systemIn);
    }

    @Test
    void canGetEmailAsAString() throws AddressException {
        //given
        String input = "imeji600@gmail.com";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        String email = userInput.email(scanner);

        //when
        String expected = "imeji600@gmail.com";

        //then
        assertThat(expected).isEqualTo(email);
    }

    @Test
    void canNotGetEmailAsAString() {
        //given
        String input = "img889989.909009";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        UserInputImpl user = new UserInputImpl();

        //when
        //then
        assertThatThrownBy(() -> user.email(scanner) )
                .isInstanceOf(AddressException.class)
                .hasMessageContaining("Email is invalid");
    }

    @Test
    void canGetSubjectAsString() {
        //given
        String input = "Subject";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        String subject = userInput.subject(scanner);

        //when
        String expected = "Subject";

        //then
        assertThat(expected).isEqualTo(subject);
    }



    @Test
    void canGetTextBodyAsString() {
        //given
        String input = "Text Body";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        String body = userInput.textBody(scanner);

        //when
        String expected = "Text Body";

        //then
        assertThat(expected).isEqualTo(body);
    }

    @Test
    @Disabled
    void canGetAttachmentsAsStringArray() {
        //Given
        String input = "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\School\\compnetworks.pdf " +
                "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf " +
                "C:\\Users\\BunnySoo\\Downloads\\Salif-cover-letter.pdf " +
                "C:\\Users\\BunnySoo\\Documents\\Job resume\\Iverson Tech Resume.pdf ";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        String[] body = userInput.attachments(scanner);

        //When
        String[] list = {"C:\\Users\\BunnySoo\\Documents\\Computer Science books\\School\\compnetworks.pdf",
                "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf",
                "C:\\Users\\BunnySoo\\Downloads\\Salif-cover-letter.pdf",
                "C:\\Users\\BunnySoo\\Documents\\Job resume\\Iverson Tech Resume.pdf"
        };

        //Then
        assertThat(body).isEqualTo(list);
        //assertThat(body.length).isEqualTo(list.length);
    }

    @Test
    @Disabled
    void canGetInlineElements() {
    }
}