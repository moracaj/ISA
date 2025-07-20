package com.project.onlybuns.service;

import com.project.onlybuns.dto.UserDto;
import com.project.onlybuns.config.JwtAuthenticationFilter;

import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.UserRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RegisteredUserRepository registeredUserRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final EmailService emailService;


    @Autowired
    public UserService(UserRepository userRepository, RegisteredUserRepository registeredUserRepository, JwtAuthenticationFilter jwtAuthenticationFilter, EmailService emailService) {
        this.userRepository = userRepository;
        this.registeredUserRepository = registeredUserRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.emailService = emailService;
       // this.bloomFilter =  new BloomFilter(1000, 3);
    }



    // Metoda za vraćanje svih korisnika

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        updatePasswords(); // Ova metoda će se izvršiti prilikom pokretanja aplikacije
    }

    public List<User> searchUsers(String name, String surname, String email) {
        return userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(name, surname, email);
    }

    public List<User> findUsersByPostsCount(int min, int max) {
        return userRepository.findByPostsCountBetween(min, max);
    }

    public void updatePasswords() {
        List<User> users = userRepository.findAll(); // Uzmite sve korisnike
        for (User user : users) {
            // Proverite da li je lozinka već BCrypt
            if (!user.getPassword().startsWith("$2a$")) { // BCrypt lozinke počinju sa $2a$
                String encodedPassword = passwordEncoder.encode(user.getPassword()); // Šifrujte lozinku
                user.setPassword(encodedPassword);
                userRepository.save(user); // Sačuvajte izmenjenog korisnika
            }
        }
    }

    public void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user); // Čuvanje korisnika u bazi
    }

    public List<User> findAllUsers() {
        return userRepository.findAll(); // Ova metoda bi trebala vraćati sve korisnike kao User
    }

    public RegisteredUser save(RegisteredUser user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }


    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }
    public User findUserByUsername(String username) {
        return  userRepository.findUserByUsername(username);
    }

    public Optional<User> findByEmail(String username) {
        return  userRepository.findByEmail(username);
    }

    public Optional<RegisteredUser> findByUsername1(String username) {
        return registeredUserRepository.findByUsername(username);
    }


    public void save(User user) {
        userRepository.save(user); // Čuvanje korisnika u bazi
    }

    public List<User> findAll() {
        return userRepository.findAll(); // Vraća listu svih korisnika iz baze
    }




    public boolean verifyOldPassword(String username, String oldPassword) {
        RegisteredUser user = registeredUserRepository.findByUsername2(username);
        if (user != null) {
            return passwordEncoder.matches(oldPassword, user.getPassword());
        }
        return false;
    }

    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        RegisteredUser user = registeredUserRepository.findByUsername2(username);
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));  // Enkriptovanje nove šifre
            registeredUserRepository.save(user);
            return true;
        }
        return false; // Ako stara šifra nije tačna
    }


    public void changeEmail(String username,String email){
        RegisteredUser user = registeredUserRepository.findByUsername2(username);
        user.setEmail(email);
        userRepository.save(user);
    }


    public void updateAdress(String username, String adress) {
        // Find the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Update the first name
        user.setAddress(adress);

        // Save the updated user
        userRepository.save(user);
    }

    @Transactional
    public void registerUser(UserDto userDTO) throws MessagingException {
        try {
            Thread.sleep(1000); // Simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
       // if (bloomFilter.contains(userDTO.getUsername())) {
            // The username is likely taken, so check in the database as well
            if (userRepository.existsByUsername(userDTO.getUsername())) {
                throw new RuntimeException("Username is already taken.");
           // }
        }

        // Add the username to the Bloom filter


        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalStateException("Email is already in use!");
        }
       // bloomFilter.add(userDTO.getUsername());

        String encodedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername(userDTO.getUsername());
        registeredUser.setPassword(encodedPassword);
        registeredUser.setEmail(userDTO.getEmail());
        registeredUser.setFirstName(userDTO.getFirstName());
        registeredUser.setLastName(userDTO.getLastName());
        registeredUser.setAddress(userDTO.getAddress());
        registeredUser.setActive(false);

        userRepository.save(registeredUser);

        String token = jwtAuthenticationFilter.generateTokenWithExpiration(registeredUser,600000);

        System.out.println("Generisani token: " + token);


        emailService.sendActivationEmail(userDTO.getEmail(), token);
    }
}



