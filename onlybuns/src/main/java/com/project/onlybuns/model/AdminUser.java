package com.project.onlybuns.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("admin")
public class AdminUser extends User {

    private String email;

    private boolean isActive;

    public AdminUser() {
        super("", "");
        this.isActive = false;
    }

    public AdminUser(String username, String password, String email) {
        super(username, password);
        this.email = email;
        this.isActive = false;
    }

    // ... ostali metodi i getteri/setteri
}
