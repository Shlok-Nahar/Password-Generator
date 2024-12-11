package com.password.generator;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main 
{

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final SecureRandom random = new SecureRandom();
    private static final int PASSWORD_LENGTH = 10 + random.nextInt(13);
    private static final String JSON_FILE_PATH = "generated_passwords.json";

    public static void main(String[] args) 
    {
        setupLogger();

        try (Scanner scanner = new Scanner(System.in)) 
        {
            logger.info("Enter the Username or Email to save with the password: ");
            String username = scanner.next();

            List<PasswordRecord> passwordList = new ArrayList<>();

            String generatedPassword = generatePassword(PASSWORD_LENGTH);

            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

            logger.info(() -> String.format("Generated Password: %s", generatedPassword));

            PasswordRecord passwordRecord = new PasswordRecord(generatedPassword, currentDate, currentTime, username);
            passwordList.add(passwordRecord);

            try 
            {
                writeToJSON(passwordList);
            } catch (IOException e) 
            {
                logger.severe("Error while writing passwords to JSON: " + e.getMessage());
            }

            logger.info("Passwords generated and saved to " + JSON_FILE_PATH);
        }
    }

    private static void setupLogger() 
    {
        Logger rootLogger = Logger.getLogger("");
        for (var handler : rootLogger.getHandlers()) 
        {
            rootLogger.removeHandler(handler);
        }

    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.ALL);
    rootLogger.addHandler(consoleHandler);
    rootLogger.setLevel(Level.ALL);
    }


    public static String generatePassword(int length) 
    {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) 
        {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public static void writeToJSON(List<PasswordRecord> passwordList) throws IOException 
    {
        File file = new File(JSON_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        if (file.exists()) 
        {
            List<PasswordRecord> existingRecords = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PasswordRecord.class));
            existingRecords.addAll(passwordList);
            mapper.writeValue(file, existingRecords);
        } 
        
        else { mapper.writeValue(file, passwordList); }
    }
}
