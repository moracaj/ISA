package com.project.onlybuns.controller;


import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;

import com.project.onlybuns.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users") // Osnovna putanja za korisničke operacije
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }




    @PostMapping
    public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisteredUser user) {
        RegisteredUser createdUser = userService.save(user);
        return ResponseEntity.ok(createdUser);
    }
    @PostMapping("/update-passwords")
    public ResponseEntity<String> updatePasswords() {
        userService.updatePasswords();
        return ResponseEntity.ok("Passwords updated successfully.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/location/{username}")
    public ResponseEntity<Map<String, Double>> getUserLocation(@PathVariable String username) {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Korisnik nije pronađen
        }

        // Vraćanje latitude i longitude
        Map<String, Double> location = new HashMap<>();
        location.put("latitude", user.getLatitude());
        location.put("longitude", user.getLongitude());

        return ResponseEntity.ok(location);
    }


}
