package com.project.onlybuns.controller;

import com.project.onlybuns.model.User;
import com.project.onlybuns.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService) {
        this.userService = userService;
    }

    // Dohvatanje svih korisnika
    @GetMapping
    public ResponseEntity<List<User>> retrieveAllAccounts() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    // Dodavanje novog korisnika
    @PostMapping
    public ResponseEntity<User> registerAccount(@RequestBody User userData) {
        User newUser = userService.saveRegisteredUser(userData);
        return ResponseEntity.ok(newUser);
    }

    // Masovno ažuriranje lozinki (npr. reset svih lozinki)
    @PostMapping("/reset-passwords")
    public ResponseEntity<String> resetAllPasswords() {
        userService.updatePasswords();
        return ResponseEntity.ok("All passwords have been updated.");
    }

    // Brisanje korisnika po ID-u
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeAccount(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Pomoćna metoda za proveru administratorskih prava
    private boolean isAdministrator(HttpSession session) {
        Object role = session.getAttribute("userType");
        return role != null && role.equals("ADMIN");
    }
}
