package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;

import com.project.onlybuns.dto.UserDto;
import com.project.onlybuns.model.UserType;
import com.project.onlybuns.service.LogInService;
import com.project.onlybuns.service.UserService;
import io.jsonwebtoken.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;



//import com.project.onlybuns.DTO.UserDTO;
import com.project.onlybuns.model.User;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.UserService;
import io.jsonwebtoken.security.Keys;
//import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.util.*;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserService registeredUserService;

    //@Autowired
    //private EmailService emailService;

    @Autowired
    private LogInService loginAttemptService;

    User registeredUser;
    @Autowired
    private PasswordEncoder passwordEncoder;


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public AuthController(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }



   /* @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errorMessages);
        }

        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("username", "Error: Username is already taken!"));
        }

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("email", "Error: Email is already in use!"));
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        User registeredUser = new User();
        registeredUser.setUsername(user.getUsername());
        registeredUser.setPassword(encodedPassword);
        registeredUser.setEmail(user.getEmail());
        registeredUser.setFirstName(user.getFirstName());
        registeredUser.setLastName(user.getLastName());
        registeredUser.setAddress(user.getAddress());
        registeredUser.setUserType(UserType.ROLE_REGISTERED);


        // Korisnik je inicijalno inaktiviran
       // registeredUser.setActive(false);
        userService.saveRegisteredUser(registeredUser);

        // Generiši token za aktivaciju
     //   String token = jwtAuthenticationFilter.generateToken(registeredUser);

       // System.out.println("Generisani token: " + token);


        //emailService.sendActivationEmail(userDTO.getEmail(), token);
      //  String encodedPassword = passwordEncoder.encode(user.getPassword());
        //System.out.println("Encoded password during registration: " + encodedPassword);

        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully!"));
    }*/


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDto userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errorMessages);
        }

        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("username", "Error: Username is already taken!"));
        }

        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("email", "Error: Email is already in use!"));
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User registeredUser = new User();
        registeredUser.setUsername(userDTO.getUsername());
        registeredUser.setPassword(encodedPassword);
        registeredUser.setEmail(userDTO.getEmail());
        registeredUser.setFirstName(userDTO.getFirstName());
        registeredUser.setLastName(userDTO.getLastName());
        registeredUser.setAddress(userDTO.getAddress());

        // Korisnik je inicijalno inaktiviran
       // registeredUser.setActive(false);
        userService.saveRegisteredUser(registeredUser);

        // Generiši token za aktivaciju
        String token = jwtAuthenticationFilter.generateToken(registeredUser);

        System.out.println("Generisani token: " + token);


      //  emailService.sendActivationEmail(userDTO.getEmail(), token);

        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully!"));
    }




  /*  @PostMapping("/login") // POST "/auth/login"
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userInput, @RequestHeader(value = "Authorization", required = false) String authHeader, HttpServletRequest request) {
        try {
            // Ako već postoji Authorization header (token), vrati poruku da je korisnik već ulogovan
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("message", "Error: User is already logged in!"));
            }

            String email = userInput.get("email");
            String password = userInput.get("password");

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Email must be provided!"));
            }

            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Password must be provided!"));
            }

            // Get client IP address
            String ipAddress = request.getRemoteAddr();

            // Check if this IP has exceeded the login attempt limit
            if (loginAttemptService.isBlocked(ipAddress)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("message", "Error: Too many login attempts. Please try again later."));
            }

            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();




                if (passwordEncoder.matches(password, existingUser.getPassword())) {
                    //String userType = existingUser instanceof User ? "ADMIN" : "REGISTERED";
                    String userType = existingUser.getUserType().toString();
                    String token = jwtAuthenticationFilter.generateToken(existingUser);

                    // Reset login attempts upon successful login
                    loginAttemptService.resetAttempts(ipAddress);

                    // Prepare response
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User logged in successfully!");
                    response.put("userType", userType);
                    response.put("token", token);

                    return ResponseEntity.ok(response);
                } else {
                    // Increment failed attempts
                    loginAttemptService.incrementAttempts(ipAddress);

                    return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Invalid password!"));
                }
            } else {
                // Increment failed attempts
                loginAttemptService.incrementAttempts(ipAddress);

                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: User not found!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error: An unexpected error occurred."));
        }
    }
*/
    ///////////////////////////////////
 @PostMapping("/login")
  public ResponseEntity<?> loginUser(@RequestBody Map<String, String> userInput, @RequestHeader(value = "Authorization", required = false) String authHeader, HttpServletRequest request) {
      try {
          // Ako već postoji Authorization header (token), vrati poruku da je korisnik već ulogovan

          String email = userInput.get("email");
          String password = userInput.get("password");

          if (email == null || email.isEmpty()) {
              return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Email must be provided!"));
          }

          if (password == null || password.isEmpty()) {
              return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Password must be provided!"));
          }

          // Get client IP address
          String ipAddress = request.getRemoteAddr();

          // Check if this IP has exceeded the login attempt limit
          if (loginAttemptService.isBlocked(ipAddress)) {
              return ResponseEntity.status(HttpStatus.FORBIDDEN)
                      .body(Collections.singletonMap("message", "Error: Too many login attempts. Please try again later."));
          }

          Optional<User> optionalUser = userService.findByEmail(email);

          if (optionalUser.isPresent()) {
              User existingUser = optionalUser.get();


              System.out.println("Email from request: " + email);
              System.out.println("Password entered: " + password);
              System.out.println("Password from DB: " + existingUser.getPassword());
              System.out.println("BCrypt matches: " + passwordEncoder.matches(password, existingUser.getPassword()));


              // Proveri da li je lozinka ispravno upoređena sa onom u bazi
              if (passwordEncoder.matches(password, existingUser.getPassword())) {

                  String userType = existingUser.getUserType().toString();
                  String token = jwtAuthenticationFilter.generateToken(existingUser);

                  // Reset login attempts upon successful login
                  loginAttemptService.resetAttempts(ipAddress);

                  // Prepare response
                  Map<String, String> response = new HashMap<>();
                  response.put("message", "User logged in successfully!");
                  response.put("userType", userType);
                  response.put("token", token);

                  return ResponseEntity.ok(response);
              } else {
                  // Increment failed attempts
                  loginAttemptService.incrementAttempts(ipAddress);

                  return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Invalid password!"));
              }
          } else {
              // Increment failed attempts
              loginAttemptService.incrementAttempts(ipAddress);

              return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: User not found!"));
          }
      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(Collections.singletonMap("message", "Error: An unexpected error occurred."));
      }
  }




    @PostMapping("/updatePasswords")
    public ResponseEntity<?> updatePasswords() {
        updateOldPasswords();

        List<User> users = registeredUserService.findAll();
        // Update postsCount for each user
        for (User user : users) {
            int postsCount = user.getPosts() != null ? user.getPosts().size() : 0;
            user.setPostsCount(postsCount);
            // Save updated user with postsCount
            registeredUserService.saveRegisteredUser(user);
        }
        return ResponseEntity.ok("Old passwords updated successfully!");
    }

    public void updateOldPasswords() {
        List<User> users = userService.findAll();

        for (User user : users) {
            // Only update passwords if they’re not encoded
            if (!user.getPassword().startsWith("$2a$")) {
                String newPassword = "sifra123";
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                userService.saveRegisteredUser(user);
            }
        }
    }







    ///////////////////////////////





/*

    @PostMapping("/updatePasswords")
    public ResponseEntity<?> updatePasswords() {
        updateOldPasswords();

        List<User> users = registeredUserService.findAll();

        // Ažuriranje postsCount za svakog korisnika
        for (User user : users) {
            int postsCount = user.getPosts() != null ? user.getPosts().size() : 0;
            user.setPostsCount(postsCount);

//            int likesCount = user.getLikes() != null ? user.getLikes().size() : 0; // Pretpostavljamo da postoji lista likes za korisnika
  //          user.setLikesCount(likesCount);

            registeredUserService.saveRegisteredUser(user); // Sačuvaj korisnika sa ažuriranim postsCount
        }
        return ResponseEntity.ok("Old passwords updated successfully!");
    }

    public void updateOldPasswords() {
        List<User> users = userService.findAll(); // Uzimanje svih korisnika
        for (User user : users) {
            // Proveri da li je trenutna lozinka "pa"
            if (user.getPassword().equals("password123")) {
                // Dodeli novu lozinku
                String newPassword = "sifra123"; // Postavi novu lozinku
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                userService.saveRegisteredUser(user); // Sačuvaj ažuriranog korisnika
            }
        }
    }*/



    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(Collections.singletonMap("message", "User logged out successfully!"));
    }





}
