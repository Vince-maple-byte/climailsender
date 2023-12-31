package com.command.mail.sender.input;

import jakarta.mail.internet.AddressException;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
    void canGetATextBodyFromTextFile() {
        String input = "\"C:\\Users\\BunnySoo\\Desktop\\dadjoke.txt\"";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        String body = userInput.textBody(scanner);

        //when
        String expected = "Singing in the shower is fun until you get soap in your mouth. Then it's a soap opera.";

        //then
        assertThat(expected).isEqualTo(body);
    }

    @Test
    void canNotGetATestBodyFromTextFileSinceItDoesNotExist() {
        String input = "C:\\Users\\BunnySoo\\Document\\dadjoke.txt";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        String body = userInput.textBody(scanner);

        //when
        String expected = "File: " + input + " does not exist";

        //then
        assertThat(expected).isEqualTo(body);
    }

    //This test does not work since I can't figure out how to disable file permissions in my computer
    @Disabled
    void canNotGetATestBodyFromTextFileSinceThePermissionsAreHidden() {
        String input = "\"C:\\Users\\BunnySoo\\Documents\\Job resume\\justdance.txt\"";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        String body = userInput.textBody(scanner);

        //when
        String expected = "File: " + input + " does not have reading permission.";

        //then
        assertThat(expected).isEqualTo(body);
    }

    @Test
    void canGetAttachmentsAsStringArray() {
        //Given
        String input = "\"C:\\Users\\BunnySoo\\Documents\\Computer Science books\\School\\compnetworks.pdf\" \n" +
                "\"C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf\" \n" +
                "\"C:\\Users\\BunnySoo\\Downloads\\Salif-cover-letter.pdf\" \n" +
                "\"C:\\Users\\BunnySoo\\Documents\\Job resume\\Iverson Tech Resume.pdf\" \n" +
                "q";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> body = userInput.inputFileNames(scanner, false);

        //When
        ArrayList<String> list = new ArrayList<>();

        list.add("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\School\\compnetworks.pdf");
        list.add("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf");
        list.add("C:\\Users\\BunnySoo\\Downloads\\Salif-cover-letter.pdf");
        list.add("C:\\Users\\BunnySoo\\Documents\\Job resume\\Iverson Tech Resume.pdf");

        //Then
        assertThat(body).isEqualTo(list);
    }

    @Test
    void canGetContentIdFromHtmlText(){
        //Given
        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Document</title>
                </head>
                <body>
                    <img src="cid:unique-img" alt="">
                    <p>Lorem ipsum, dolor sit amet consectetur adipisicing elit. Necessitatibus repellendus est id harum eos atque esse cumque. Incidunt accusamus\s
                        sint laboriosam magni veritatis architecto voluptas? Reprehenderit consequuntur eius qui accusamus.</p>
                    <img src="cid:unique-img2" alt="">
                </body>
                </html>""";
        //When
        String[] contentId = userInput.getContentIdFromHtml(html);
        //Then
        assertThat(contentId.length).isEqualTo(2);
        assertThat(contentId[0]).isEqualTo("unique-img");
        assertThat(contentId[1]).isEqualTo("unique-img2");
    }

    @Test
    void canGetInlineElementsFromUserInput(){
        //Given
        String input = """
                "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\School\\compnetworks.pdf"
                 "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf"\s
                 "C:\\Users\\BunnySoo\\Downloads\\Salif-cover-letter.pdf"
                 "C:\\Users\\BunnySoo\\Documents\\Job resume\\Iverson Tech Resume.pdf"
                quit""";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> body = userInput.inputFileNames(scanner, true);

        //When
        ArrayList<String> list = new ArrayList<>();

        list.add("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\School\\compnetworks.pdf");
        list.add("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf");
        list.add("C:\\Users\\BunnySoo\\Downloads\\Salif-cover-letter.pdf");
        list.add("C:\\Users\\BunnySoo\\Documents\\Job resume\\Iverson Tech Resume.pdf");

        //Then
        assertThat(body).isEqualTo(list);
    }
}