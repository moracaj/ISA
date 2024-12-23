package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;

import com.project.onlybuns.dto.PostDto;
import com.project.onlybuns.service.ImageUploadService;
import com.project.onlybuns.model.*;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.CommentService;
import com.project.onlybuns.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/posts") // Base path for post-related endpoints
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    private final UserService userService;

    private final ImageUploadService imageUploadService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, UserService userService, ImageUploadService imageUploadService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }


    public String getUsernameFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName(); // Dobijamo korisničko ime iz JWT tokena
        }
        return null;
    }

//    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('REGISTERED')")  // Ova anotacija osigurava da samo REGISTERED korisnici mogu obrisati objavu
//    public ResponseEntity<String> deletePost(@PathVariable Long id) {
//        // Dobijanje korisničkog imena iz tokena
//        String username = getUsernameFromToken();
//
//        if (username == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated"); // 401 if user is not authenticated
//        }
//
//        // Pronađi post po ID-u
//        Optional<Post> optionalPost = postService.findById(id);
//
//        if (optionalPost.isPresent()) {
//            Post post = optionalPost.get();
//
//            // Proveri da li je korisnik autor objave
//            if (post.getUser().getUsername().equals(username)) {
//                postService.delete(id); // Obriši post
//                return ResponseEntity.noContent().build(); // 204 if post was deleted successfully
//            } else {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of this post and cannot delete it"); // 403 if user is not the author
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found"); // 404 if post doesn't exist
//        }
//    }


    @GetMapping("/my-posts")
    @PreAuthorize("hasRole('REGISTERED')")
    public List<Map<String, Object>> getPostsForLoggedInUser() {
        // Dobijanje trenutnog korisnika iz SecurityContext-a
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Post> posts = postService.getPostsByUsername(username);
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> postData = new HashMap<>();

            // Dodajemo podatke o postu
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown"); // Dodajemo korisničko ime
            postData.put("countLikes", post.getLikesCount());
            // Dodajemo listu komentara
            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown"); // Dodajemo korisničko ime komentara
                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            // Dodajemo post sa komentarima
            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
    }





    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("src/main/resources/static.images/" + imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @PostMapping("/create")
//    @PreAuthorize("hasRole('REGISTERED')")
//    public ResponseEntity<Post> createPost(@RequestBody PostDto  postDto) {
//        // Dobijanje trenutnog korisnika iz SecurityContext-a
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User loggedUser = userService.findByUsername(username).orElse(null);
//        System.out.println("Korisnik: " + loggedUser.getEmail());
//
//
//        if (loggedUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        // Kreiraj novi Post objekat i postavi polja iz DTO-a
//        Post post1 = new Post();
//        post1.setDescription(postDto.getDescription());
//        post1.setImageUrl(postDto.getImageUrl());
//        post1.setUser(loggedUser);
//        post1.setCreatedAt(LocalDateTime.now()); // Automatsko postavljanje trenutnog vremena
//        System.out.println("Opis posta: " + post1.getDescription());
//        // Spasi post u bazu
//        Post savedPost = postService.save(post1);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
//   }
@PostMapping("/create")
@PreAuthorize("hasRole('REGISTERED')")
public ResponseEntity<Post> createPost(
        @RequestParam("description") String description,
        @RequestParam("latitude") Double latitude,
        @RequestParam("longitude") Double longitude,
        @RequestParam("location") String location,
        @RequestPart("imageFile") MultipartFile imageFile) {

    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User loggedUser = userService.findByUsername(username).orElse(null);

    if (loggedUser == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    String imageUrl;
    try {
        imageUrl = imageUploadService.uploadImage(imageFile);
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    Post post = new Post();
    post.setDescription(description);
    post.setImageUrl(imageUrl);
    post.setUser(loggedUser);
    post.setCreatedAt(LocalDateTime.now());
    post.setLatitude(latitude);
    post.setLongitude(longitude);
    post.setLocation(location);

    Post savedPost = postService.save(post);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
}







    @GetMapping("/allPosts")
    public List<Map<String, Object>> getAllPosts() {
        List<Post> posts = postService.findAll();
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"); // Format za datum i vreme

        for (Post post : posts) {
            Map<String, Object> postData = new HashMap<>();

            // Dodajemo podatke o postu
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown");
            postData.put("countLikes", post.getLikesCount());
            // Formatiramo datum i vreme za post
            String formattedDate = post.getCreatedAt() != null ? post.getCreatedAt().format(formatter) : "Unknown date";
            postData.put("createdAt", formattedDate); // Dodajemo datum i vreme

            // Dodajemo listu komentara
            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown");

                // Formatiramo datum i vreme za komentar
                String formattedCommentDate = comment.getCreatedAt() != null ? comment.getCreatedAt().format(formatter) : "Unknown date";
                commentData.put("createdAt", formattedCommentDate); // Dodajemo datum i vreme za komentar

                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            // Dodajemo post sa komentarima
            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
    }





    /*@PostMapping("/{id}/comments")
    public ResponseEntity<Comment>addComment(@PathVariable Long id, @RequestBody Comment comment, @AuthenticationPrincipal User user) {
        comment.setUser(user);
        comment.setPost(postService.findById(id).orElse(null)); // Setuj post za komentar
        Comment savedComment = commentService.save(comment); // Implementiraj ovu metodu u servisu
        return ResponseEntity.ok(savedComment);
    }*/
}