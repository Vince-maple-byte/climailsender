package com.command.mail.sender.commands;

import com.command.mail.sender.input.FileCreationImpl;
import com.command.mail.sender.input.UserInputImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.shell.Shell;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
    @ShellMethod(key = "simpleMessage", value="This command sends an simple email to the specified " +
            "email address.\n You cannot include attachments with this command, " +
            "to do this use sendAttachments.")
    public void simpleMessage(
            @ShellOption(value = {"-t", "--textFile"}, defaultValue = "")
            String fileInputLocation) throws MessagingException {

        Scanner scanner = new Scanner(System.in);
        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(send, true);

        message.setTo(userInput.email(scanner));
        message.setSubject(userInput.subject(scanner));

        if(fileInputLocation.isEmpty())
            message.setText(userInput.textBody(scanner), false);
        else
            message.setText(userInput.fileToTextBody(fileInputLocation));

        javaMailSender.send(send);
        System.out.println("Successfully sent the email");
    }

    /*
        Shell options I want add here:
    *  attachmentDir: Have a directory for all of the attachments that the user wants to add
    *  instead of typing it out one at a time
    *  Useful links
    *   https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html
    *  https://docs.oracle.com/javase/8/docs/api/java/nio/file/DirectoryStream.html
    *   https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html
    */
    @Override
    @ShellMethod(key = "sendAttachments", value="This command sends an email to the specified email address" +
            "with attachments included")
    public void sendAttachments(
            @ShellOption(value = {"-t", "--textFile"}, defaultValue = "") String fileInputLocation,
            @ShellOption(value = {"-d", "attachFolder"}, defaultValue = "") String attachFolder
    ) throws MessagingException, FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(send, true);

        message.setTo(userInput.email(scanner));
        message.setSubject(userInput.subject(scanner));
        if(fileInputLocation.isEmpty())
            message.setText(userInput.textBody(scanner), false);
        else
            message.setText(userInput.fileToTextBody(fileInputLocation));

        System.out.println("When entering file attachments/inline elements, " +
                "we can not guarantee if the files would go through do to email server limitations");

        ArrayList<String> attachmentString = userInput.attachments(scanner);
        if(attachFolder.isEmpty()){
            ArrayList<File> attachmentList = fileCreation.createFileFromString(attachmentString);
            for (File file : attachmentList) {
                message.addAttachment(file.getName(), file);
            }
        }
        else {
            Path dir = Paths.get(attachFolder);
            List<Path> files = filesInDirectory(dir);
            for(Path path : files){
                File file = path.toFile();
                message.addAttachment(file.getName(), file);
            }
        }

        javaMailSender.send(send);
        System.out.println("Successfully sent the email");
    }

    /*
        Shell options I want add here:
     * textFile: gets the text body from a .txt file
     * attachmentDir: Have a directory for all of the attachments that the user wants to add
     *      instead of typing it out one at a time
     * inlineElementDir: Have a directory for all of the inline elements that the user wants to add
     *      instead of typing it out one at a time
     */
    @Override
    @ShellMethod(key = "sendHtml", value="This command sends an html emails with images, videos, etc" +
            " being embedded into it. ")
    public void sendHtml(
            @ShellOption(value = {"-f", "--htmlFile"}, defaultValue = "") String htmlFileLocation,
            @ShellOption(value = {"-a", "--attach"}, defaultValue = "false") boolean attach,
            @ShellOption(value = {"-d", "--attachFolder"}, defaultValue = "") String attachFolder,
            @ShellOption(value = {"-i", "--inlineFolder"}, defaultValue = "") String inlineFolder
    ) throws MessagingException, IOException {
        Scanner scanner = new Scanner(System.in);
        MimeMessage send = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(send, true);

        message.setTo(userInput.email(scanner));
        message.setSubject(userInput.subject(scanner));

        /*What is going to be done here:
         * Get the html file path and Make a file object createHtmlFile()
         * Convert the contents of the html file object into a String convertHtmlFileIntoAString()
         *
         * get the content-id from the html String getContentIdFromHtml()
         * get the inline element file inlineElements()
         *
         * use a while loop to add each inline elements message.addInline()
         *
         */

        String[] contentId;
        ArrayList<String> inlineElements;
        File htmlFile;
        ArrayList<File> inlineFiles = new ArrayList<>();

        if(htmlFileLocation.isEmpty()) htmlFile = fileCreation.createHtmlFile(scanner);
        else {
            htmlFile = new File(htmlFileLocation); //User needs to use / in the file path or \\
        }

        String htmlText = fileCreation.convertHtmlFileIntoAString(htmlFile);
        contentId = userInput.getContentIdFromHtml(htmlText);
        message.setText(htmlText, true);


        inlineElements = userInput.inlineElements(scanner, contentId.length);

        if(inlineFolder.isEmpty()){
            inlineFiles = fileCreation.createFileFromString(inlineElements);
        }
        else {
            Path dir = Paths.get(inlineFolder);
            List<Path> files = filesInDirectory(dir);
            for(Path file : files){
                inlineFiles.add(file.toFile());
            }
        }

        for(int i = 0; i < contentId.length; i++){
            if(i >= inlineFiles.size()){
                break;
            }
            else {
                message.addInline(contentId[i], inlineFiles.get(i));
            }
        }

        //This code below allows for the user to add attachments to their email.
        if(attachFolder.isEmpty()){
            String ans = "";
            if(!attach){
                System.out.println("Do you want to enter an attachment? yes/no");
                ans = scanner.next();
            }
            if(ans.equalsIgnoreCase("yes") || attach){
                ArrayList<String> attachmentString = userInput.attachments(scanner);
                ArrayList<File> attachmentList = fileCreation.createFileFromString(attachmentString);
                for (File file : attachmentList) {
                    message.addAttachment(file.getName(), file);
                }
            }
        }
        else{
            Path dir = Paths.get(attachFolder);
            List<Path> paths = filesInDirectory(dir);
            for(Path path : paths) {
                File file = path.toFile();
                message.addAttachment(file.getName(), file);
            }
        }



        javaMailSender.send(send);
        System.out.println("Successfully sent the email");
    }

    //With this method we are able to get the individual files in the directory
    private static List<Path> filesInDirectory(Path dir){
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
            for(Path path : stream){
                files.add(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return files;
    }

}
