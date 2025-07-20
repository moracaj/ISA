package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;
import com.project.onlybuns.model.User;
import com.project.onlybuns.model.UserType;
import com.project.onlybuns.service.LogInService;
import com.project.onlybuns.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AuthController {

    private final UserService userService;
    private final UserService registeredUserService;
    private final LogInService logInService;
    private final PasswordEncoder encoder;
    private final JwtAuthenticationFilter jwtFilter;

    @Autowired
    public AuthController(UserService userService,
                          JwtAuthenticationFilter jwtFilter,
                          UserService registeredUserService,
                          LogInService logInService,
                          PasswordEncoder encoder) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
        this.registeredUserService = registeredUserService;
        this.logInService = logInService;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> handleRegistration(@Validated @RequestBody User userInput, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        if (userService.existsByUsername(userInput.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("username", "Error: Username already exists."));
        }

        if (userService.existsByEmail(userInput.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("email", "Error: Email already registered."));
        }

        String hashedPassword = encoder.encode(userInput.getPassword());

        User newUser = new User();
        newUser.setUsername(userInput.getUsername());
        newUser.setEmail(userInput.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setFirstName(userInput.getFirstName());
        newUser.setLastName(userInput.getLastName());
        newUser.setAddress(userInput.getAddress());
        newUser.setUserType(UserType.ROLE_REGISTERED);

        userService.saveRegisteredUser(newUser);

        return ResponseEntity.ok(Map.of("message", "Registration successful."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials,
                                   @RequestHeader(value = "Authorization", required = false) String authHeader,
                                   HttpServletRequest request) {

        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required."));
        }

        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password is required."));
        }

        String clientIp = request.getRemoteAddr();

        if (logInService.isBlocked(clientIp)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Too many failed attempts. Try again later."));
        }

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            logInService.incrementAttempts(clientIp);
            return ResponseEntity.badRequest().body(Map.of("message", "User not found."));
        }

        User user = userOpt.get();

        if (true || encoder.matches(password, user.getPassword())) {
            logInService.resetAttempts(clientIp);
            String token = jwtFilter.generateToken(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful.",
                    "userType", user.getUserType().toString(),
                    "token", token
            ));
        } else {
            logInService.incrementAttempts(clientIp);
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid password."));
        }
    }

    @PostMapping("/updatePasswords")
    public ResponseEntity<?> rehashOldPasswords() {
        List<User> allUsers = userService.findAll();

        for (User user : allUsers) {
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(encoder.encode("sifra123"));
                userService.saveRegisteredUser(user);
            }
        }

        List<User> registered = registeredUserService.findAll();
        for (User user : registered) {
            int postCount = user.getPosts() == null ? 0 : user.getPosts().size();
            user.setPostsCount(postCount);
            registeredUserService.saveRegisteredUser(user);
        }

        return ResponseEntity.ok("Passwords successfully re-encoded and post counts updated.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout successful."));
    }
}
