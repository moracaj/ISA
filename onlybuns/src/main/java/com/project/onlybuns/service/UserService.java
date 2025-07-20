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

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @PostConstruct
    public void secureAllPasswords() {
        List<User> allUsers = userRepo.findAll();
        for (User user : allUsers) {
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(encoder.encode(user.getPassword()));
                userRepo.save(user);
            }
        }
    }

    public void createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public void removeUserById(Long userId) {
        userRepo.deleteById(userId);
    }

    public boolean usernameTaken(String username) {
        return userRepo.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepo.existsByEmail(email);
    }

    public Optional<User> getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public Optional<User> getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<User> filterUsersByPostsRange(int minPosts, int maxPosts) {
        return userRepo.findByPostsCountBetween(minPosts, maxPosts);
    }

    public List<User> searchByNameOrEmail(String first, String last, String email) {
        return userRepo.findByFirstNameContainingOrLastNameContainingOrEmailContaining(first, last, email);
    }

    public List<User> getAllRegistered() {
        return userRepo.findByUserType(UserType.ROLE_REGISTERED);
    }

    public List<User> getAllAdmins() {
        return userRepo.findByUserType(UserType.ROLE_ADMIN);
    }

    public Optional<User> getRegisteredByUsername(String username) {
        return userRepo.findRegisteredUserByUsername(username, UserType.ROLE_REGISTERED);
    }

    public User persistRegistered(User user) {
        user.setUserType(UserType.ROLE_REGISTERED);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User persistAdmin(User user) {
        user.setUserType(UserType.ROLE_ADMIN);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
