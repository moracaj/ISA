package com.project.onlybuns.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {

    private String userName;
    private String passcode;
    private String emailAddress;

    @JsonProperty("first_name")
    private String givenName;

    @JsonProperty("last_name")
    private String familyName;

    private String residence;

    // Default constructor
    public UserDto() {
    }

    // All-args constructor
    public UserDto(String userName, String passcode, String emailAddress, String givenName, String familyName, String residence) {
        this.userName = userName;
        this.passcode = passcode;
        this.emailAddress = emailAddress;
        this.givenName = givenName;
        this.familyName = familyName;
        this.residence = residence;
    }

    // --- Getters and Setters ---

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }
}
