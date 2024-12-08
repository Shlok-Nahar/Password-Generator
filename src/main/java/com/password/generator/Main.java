package com.password.generator;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    private static final int PASSWORD_LENGTH = 12;
    private static final String JSON_FILE_PATH = "generated_passwords.json";

    public static void main(String[] args) {
        // Generate password and log the message only if INFO level is enabled
        String generatedPassword = generatePassword(PASSWORD_LENGTH);
        
        // Get current date and time
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        
        // Log the generated password
        logger.info(() -> String.format("Generated Password: %s", generatedPassword));

        // Create a PasswordRecord and write to JSON
        PasswordRecord passwordRecord = new PasswordRecord(generatedPassword, currentDate, currentTime, "");

        try {
            writeToJSON(passwordRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Method to write PasswordRecord to JSON file
    public static void writeToJSON(PasswordRecord passwordRecord) throws IOException {
        File file = new File(JSON_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        // If the file exists, append to the existing records
        if (file.exists()) {
            List<PasswordRecord> records = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PasswordRecord.class));
            records.add(passwordRecord);
            mapper.writeValue(file, records); // Write the updated list to the file
        } else {
            // If the file does not exist, create a new one and add the record
            mapper.writeValue(file, List.of(passwordRecord));
        }
    }
}
