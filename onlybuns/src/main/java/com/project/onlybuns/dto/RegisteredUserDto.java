package com.project.onlybuns.dto;

import java.util.List;


public class RegisteredUserDto {
    private String firstName;
    private String lastName;
    private String email;

    private  String username;
    private int postsCount;

    private String confirmPassword;



    public RegisteredUserDto(String firstName, String lastName, String email, int postsCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.postsCount = postsCount;

    }

    public RegisteredUserDto(String firstName, String lastName, String email,String username, int postsCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.postsCount = postsCount;



    }




    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public int getPostsCount() {
        return postsCount;
    }



    // Setteri
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPostsCount(int postsCount) {

        this.postsCount = postsCount;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
