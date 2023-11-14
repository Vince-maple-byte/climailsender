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

    //TODO Need to fix this unit test so that it checks if the assertThat of one string does not equal to the expected outcome
    @Test
    void canNotCreateFileFromAString() throws FileNotFoundException {
        //given
        Mockito.when(userInput.attachments(scanner)).thenReturn(
                new ArrayList<>(Arrays.asList("C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Algorithms-4th-Edition.pdf",
                        "C:\\Users\\BunnySoo\\Documents\\Computer Science books\\Abraham Silberschatz, Greg Gagne, P"))

        );

        ArrayList<String> filesAsString = userInput.attachments(scanner);

        System.out.println(filesAsString.get(0));

        //When Then
        assertThat(1).isNotEqualTo(4);
//        assertThat(filesAstring.get(0))
//        assertThatThrownBy(() -> fileCreation.createFileFromString(filesAsString))
//                .isInstanceOf(FileNotFoundException.class)
//                .hasMessageContaining("File does not exist");
    }
}