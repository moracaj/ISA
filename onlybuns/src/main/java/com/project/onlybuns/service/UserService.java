package com.project.onlybuns.service;



import com.project.onlybuns.model.User;

import com.project.onlybuns.model.UserType;
import com.project.onlybuns.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public PasswordEncoder passwordEncoder;  //ne znam sta ce mi ovo

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        updatePasswords();
    }

    public List<User> searchUsers(String name, String surname, String email) {
        return userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(name, surname, email);
    }

    public List<User> findUsersByPostsCount(int min, int max) {
        return userRepository.findByPostsCountBetween(min, max);
    }

    public void updatePasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) {
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userRepository.save(user);
            }
        }
    }

    public void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllRegisteredUsers() {
        return userRepository.findByUserType(UserType.REGISTERED_USER);
    }

    // Metoda za dobavljanje svih administratora
    public List<User> findAllAdministrators() {
        return userRepository.findByUserType(UserType.ADMINISTRATOR);
    }

    public Optional<User> findRegisteredUsersByUsername(String username) {
        return userRepository.findRegisteredUserByUsername(username, UserType.REGISTERED_USER);
    }



   /* // Metoda za dobavljanje svih administratora
    public List<User> findAllAdministratorsById() {
        return userRepository.findByUserId(UserType.ADMINISTRATOR);
    }*/

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Funkcija za čuvanje registrovanog korisnika
    public User saveRegisteredUser(User user) {
        user.setUserType(UserType.REGISTERED_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Enkoding lozinke
        return userRepository.save(user);
    }

    // Funkcija za čuvanje administratora
    public User saveAdministrator(User user) {
        user.setUserType(UserType.ADMINISTRATOR);
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Enkoding lozinke
        return userRepository.save(user);
    }


    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
