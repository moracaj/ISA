package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Uklonjen userType atribut
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;


    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    private String address;
    private Double latitude;
    private Double longitude;

    private boolean isActive; // Da li je nalog aktiviran

    @Column(name = "posts_count")
    private Integer postsCount; // Broj objava

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    @Column(name = "registration_date")
    private LocalDateTime registrationDate; // Datum kada je pokušao da se registruje

    @Column(name = "last_active_date")
    private LocalDateTime lastActiveDate;

    public LocalDateTime getLastActiveDate() {
        return this.lastActiveDate;
    }

    public void setLastActiveDate(LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public void updateLastActiveDate() {
        this.lastActiveDate = LocalDateTime.now();
    }

    // Getters and Setters

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Getters and Setters

    public Integer getPostsCount() {
        return postsCount != null ? postsCount : 0; // Vraća 0 ako je null
    }

    public void setPostsCount(Integer postsCount) {
        this.postsCount = postsCount;
    }


    // No-argument constructor
    public User() {
        this.isActive = false; // Podrazumevano nije aktiviran
        this.postsCount =0;
        this.registrationDate = LocalDateTime.now();
    }


    // Constructor with parameters
    public User(String username, String password, String email, String firstName, String lastName, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.isActive = false; // Podrazumevano nije aktiviran
    }



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isActive = false; // Podrazumevano nije aktiviran
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUserType() {
        return this.getClass().getSimpleName(); // Vraća naziv klase (tip korisnika)
    }


}
