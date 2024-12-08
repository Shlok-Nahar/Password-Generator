package com.password.generator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordRecord {

    private String password;
    private String generatedDate;
    private String generatedTime;
    private String identification;  // New field for identification

    // Default constructor for Jackson
    public PasswordRecord() {
        this.identification = "";  // Set default empty value
    }

    // Constructor for creating the object with required fields
    @JsonCreator
    public PasswordRecord(@JsonProperty("password") String password,
                          @JsonProperty("generatedDate") String generatedDate,
                          @JsonProperty("generatedTime") String generatedTime,
                          @JsonProperty("identification") String identification) {
        this.password = password;
        this.generatedDate = generatedDate;
        this.generatedTime = generatedTime;
        this.identification = identification != null ? identification : ""; // Ensure it's not null
    }

    // Getters and Setters for each field
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(String generatedTime) {
        this.generatedTime = generatedTime;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
