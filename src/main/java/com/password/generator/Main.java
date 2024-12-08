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

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());  // Logger instance
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final SecureRandom random = new SecureRandom();
    private static final int PASSWORD_LENGTH = 10 + random.nextInt(13);
    private static final String JSON_FILE_PATH = "generated_passwords.json";

    public static void main(String[] args) {
        setupLogger();  // Set up logger configuration

        try (Scanner scanner = new Scanner(System.in)) {
            // Log the prompt for the number of passwords
            logger.info("Enter the Username or Email to save with the password: ");
            String username = scanner.next();

            // List to store generated passwords
            List<PasswordRecord> passwordList = new ArrayList<>();

            // Generate passwords and add to the list
            String generatedPassword = generatePassword(PASSWORD_LENGTH);

            // Get current date and time
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

            // Log the generated password with appropriate logging level
            logger.info(() -> String.format("Generated Password: %s", generatedPassword));

            // Create PasswordRecord and add to the list
            PasswordRecord passwordRecord = new PasswordRecord(generatedPassword, currentDate, currentTime, username);
            passwordList.add(passwordRecord);

            try {
                writeToJSON(passwordList);
            } catch (IOException e) {
                logger.severe("Error while writing passwords to JSON: " + e.getMessage());
            }

            logger.info("Passwords generated and saved to " + JSON_FILE_PATH);
        }
    }

    // Set up logger configuration
    private static void setupLogger() {
    // Remove all default handlers to avoid duplicate logs
    Logger rootLogger = Logger.getLogger("");
    for (var handler : rootLogger.getHandlers()) {
        rootLogger.removeHandler(handler);
    }

    // Add a new console handler
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.ALL);
    rootLogger.addHandler(consoleHandler);
    rootLogger.setLevel(Level.ALL);
}


    // Method to generate a password of a given length
    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    // Method to write the list of PasswordRecord to a JSON file
    public static void writeToJSON(List<PasswordRecord> passwordList) throws IOException {
        File file = new File(JSON_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        // If the file exists, append to the existing records
        if (file.exists()) {
            List<PasswordRecord> existingRecords = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PasswordRecord.class));
            existingRecords.addAll(passwordList);  // Add new records to the existing ones
            mapper.writeValue(file, existingRecords);  // Write the updated list to the file
        } else {
            // If the file does not exist, create a new one and write the password records
            mapper.writeValue(file, passwordList);
        }
    }
}
