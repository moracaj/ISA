package com.project.onlybuns.controller;

import com.project.onlybuns.dto.RegisteredUserDto;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.RegisteredUserService;
import com.project.onlybuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController// Base path for registered user-related endpoints
public class RegisteredUserController {

    private final RegisteredUserService registeredUserService;
    private final UserService registeredUserService1;

    @Autowired
    public RegisteredUserController(RegisteredUserService registeredUserService, UserService registeredUserService1) {
        this.registeredUserService = registeredUserService;
        this.registeredUserService1 = registeredUserService1;

    }


    @GetMapping("/users/{username}")
    public ResponseEntity<RegisteredUser> getUserProfile(@PathVariable String username) {
        Optional<User> optionalUser = registeredUserService1.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();  // Dobijamo User objekat

            // Proveravamo da li je User zapravo instanca RegisteredUser
            if (user instanceof RegisteredUser) {
                RegisteredUser registeredUser = (RegisteredUser) user;  // Castujemo User u RegisteredUser
                RegisteredUser convertedUser = convertToRegisteredUser(registeredUser);  // Konvertujemo
                return ResponseEntity.ok(convertedUser);
            } else {
                // Ako korisnik nije tipa RegisteredUser, možete obraditi ovu situaciju
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private RegisteredUser convertToRegisteredUser(RegisteredUser registeredUser) {
        RegisteredUser newUser = new RegisteredUser();
        newUser.setUsername(registeredUser.getUsername());
        newUser.setFirstName(registeredUser.getFirstName());
        newUser.setLastName(registeredUser.getLastName());
        newUser.setEmail(registeredUser.getEmail());

        // Lista koja će sadržati stvarne Post objekte
        List<Post> posts = new ArrayList<>();

        // Pretvaranje podataka u stvarne Post objekte
        for (Post post : registeredUser.getPosts()) {
            Post newPost = new Post();
            newPost.setId(post.getId());
            newPost.setImageUrl(post.getImageUrl());
            newPost.setDescription(post.getDescription());
            newPost.setUser(post.getUser());
            newPost.setComments(post.getComments());
            newPost.setCreatedAt(post.getCreatedAt());

            posts.add(newPost);
        }

        newUser.setPosts(posts);  // Setovanje stvarnih postova
        return newUser;
    }


    @GetMapping("/allRegisteredUsers")
    public ResponseEntity<Page<RegisteredUserDto>> getRegisteredUsers1(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Dohvati korisnike koristeći paginaciju
        Page<RegisteredUser> usersPage = registeredUserService.findAll(PageRequest.of(page, size));



        // Mapiranje na DTO
        Page<RegisteredUserDto> dtoPage = usersPage.map(user -> new RegisteredUserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getPosts().size()
        ));

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/allRegisteredUsers1")
    public ResponseEntity<List<RegisteredUserDto>> getAllRegisteredUsers() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Dohvati sve korisnike bez paginacije
        List<RegisteredUser> allUsers = registeredUserService.findAll();

        // Mapiranje na DTO listu
        List<RegisteredUserDto> userDTOs = allUsers.stream().map(user -> new RegisteredUserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getPosts().size()


        )).toList();

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/registered-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RegisteredUserDto>> getRegisteredUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + authentication.getName() + " with roles: " + authentication.getAuthorities());

        // Dohvati korisnike koristeći paginaciju
        Page<RegisteredUser> usersPage = registeredUserService.findAll(PageRequest.of(page, size));

        // Mapiranje na DTO
        Page<RegisteredUserDto> dtoPage = usersPage.map(user -> new RegisteredUserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPosts().size()

        ));

        return ResponseEntity.ok(dtoPage);
    }


    @GetMapping("/all")
    public ResponseEntity<List<RegisteredUser>> getAllUsers() {
        List<RegisteredUser> users = registeredUserService.findAll();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUser> getUserById(@PathVariable Long id) {
        return registeredUserService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegisteredUser> createUser(@RequestBody RegisteredUser user) {
        RegisteredUser createdUser = registeredUserService.save(user);
        return ResponseEntity.ok(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        registeredUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    }










