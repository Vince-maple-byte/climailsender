package com.command.mail.sender.input;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileCreationImplTest {

    @Mock
    UserInputImpl userInput;
    FileCreationImpl fileCreation;
    Scanner scanner;

    @BeforeEach
    void setUp() {
        fileCreation = new FileCreationImpl();

    }

    @AfterEach
    void tearDown() {
        System.setIn(System.in);
    }

    @Test
    @Disabled
    void canCreateFileFromAString() throws FileNotFoundException {
        //given
        ArrayList<String> listOfFileNames = new ArrayList<>();
        Mockito.when(userInput.attachments(scanner)).thenReturn(
                new ArrayList<>(Arrays.asList("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf",
                        "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Abraham Silberschatz, Greg Gagne, " +
                                "Peter B. Galvin - Operating System Concepts-Wiley (2018).pdf"))

        );
        ArrayList<String> filesAsString = userInput.attachments(scanner);
        ArrayList<File> files = fileCreation.createFileFromString(filesAsString);

        //When
        File[] filesExpected = new File[2];
        filesExpected[0] = new File("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf");
        filesExpected[1] = new File("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Abraham Silberschatz, Greg Gagne, Peter B. Galvin - Operating System Concepts-Wiley (2018).pdf");
        System.out.println(files.get(0).getName());

        //Then
        assertThat(files.get(0)).isEqualTo(filesExpected[0]);
        assertThat(files.get(1)).isEqualTo(filesExpected[1]);
    }

    @Test
    void canNotCreateFileFromAString() throws FileNotFoundException {
        //given
        ArrayList<String> list = new ArrayList<>(Arrays.asList("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf",
                "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Abraham Silberschatz, Greg Gagne, P"));
        Mockito.when(userInput.attachments(scanner)).thenReturn(list);

        ArrayList<String> filesAsString = userInput.attachments(scanner);
        ArrayList<File> files = fileCreation.createFileFromString(filesAsString);
        int correctSize = list.size();

        //When Then
        assertThat(files.size()).isNotEqualTo(list.size());
    }

    @Test
    void createHtmlFileGivesTheCorrectPathName(){
        //Given
        String htmlPathName = "C:\\Users\\BunnySoo\\Desktop\\html email\\email.html";
        String input = "\"C:\\Users\\BunnySoo\\Desktop\\html email\\email.html\"";
        //When
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        scanner = new Scanner(System.in);
        File htmlFile = fileCreation.createHtmlFile(scanner);

        //Then
        assertThat(htmlFile.getPath()).isEqualTo(htmlPathName);
    }

    @Test
    void convertHtmlFileIntoAStringGivesTheCorrectString() throws IOException {
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
        File htmlFile = new File("C:\\Users\\BunnySoo\\Desktop\\html email\\email.html");
        String htmlContents = fileCreation.convertHtmlFileIntoAString(htmlFile);
        //Then
        assertThat(normalizeLineSeparators(htmlContents)).isEqualTo(normalizeLineSeparators(html));

        //This method practically works the only difference that the given string has CRLF and the method that returns a String has LF as a line separator
    }

    // Normalize line separators to a consistent format (CRLF)
    private String normalizeLineSeparators(String input) {
        return input.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "\r\n");
    }

    @Test
    void convertHtmlFileIntoAStringGivesTheIncorrectString() throws IOException {
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
                <;;;>
                </html>""";
        //When
        File htmlFile = new File("C:\\Users\\BunnySoo\\Desktop\\html email\\email.html");
        String htmlContents = fileCreation.convertHtmlFileIntoAString(htmlFile);
        //Then
        assertThat(normalizeLineSeparators(htmlContents)).isNotEqualTo(normalizeLineSeparators(html));

        //This method practically works the only difference that the given string has CRLF and the method that returns a String has LF as a line separator
    }


}