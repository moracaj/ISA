package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;

//import com.project.onlybuns.DTO.PostDTO;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/posts") // Base path for post-related endpoints
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    private final UserService userService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }


    public String getUsernameFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName(); // Dobijamo korisničko ime iz JWT tokena
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('REGISTERED')")  // Ova anotacija osigurava da samo REGISTERED korisnici mogu obrisati objavu
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        // Dobijanje korisničkog imena iz tokena
        String username = getUsernameFromToken();

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated"); // 401 if user is not authenticated
        }

        // Pronađi post po ID-u
        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Proveri da li je korisnik autor objave
            if (post.getUser().getUsername().equals(username)) {
                postService.delete(id); // Obriši post
                return ResponseEntity.noContent().build(); // 204 if post was deleted successfully
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of this post and cannot delete it"); // 403 if user is not the author
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found"); // 404 if post doesn't exist
        }
    }


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

    @PostMapping("/create")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        // Dobijanje trenutnog korisnika iz SecurityContext-a
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.findRegisteredUsersByUsername(username).orElse(null);

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Kreiraj novi Post objekat i postavi polja iz DTO-a
        Post post1 = new Post();
        post.setDescription(post.getDescription());
        post.setImageUrl(post.getImageUrl());
        post.setUser(loggedUser);
        post.setCreatedAt(LocalDateTime.now()); // Automatsko postavljanje trenutnog vremena

        // Spasi post u bazu
        Post savedPost = postService.save(post1);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }



  /*  @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.findByUsername1(username).orElse(null);

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Optional<Post> optionalPost = postService.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            if (!post.getUser().equals(loggedUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the author of this post and cannot update it");
            }

            if (updatedPost.getImageUrl() != null) {
                post.setImageUrl(updatedPost.getImageUrl());
            }
            if (updatedPost.getDescription() != null) {
                post.setDescription(updatedPost.getDescription());
            }

            postService.update(post);

            return ResponseEntity.ok("Post updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    }*/



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


   /* @PutMapping("/{id}/like")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> likeOrUnlikePost(@PathVariable Long id) {
        // Pronađi post prema id
        Optional<Post> postOptional = postService.findById(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = postOptional.get();

        // Dohvati korisničko ime iz SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Pronađi korisnika na osnovu korisničkog imena
        Optional<User> loggedInUserOptional = userService.findByUsername1(username);

        if (loggedInUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        User loggedInUser = loggedInUserOptional.get();


        }
    }*/


    /*@PostMapping("/{id}/comments")
    public ResponseEntity<Comment>addComment(@PathVariable Long id, @RequestBody Comment comment, @AuthenticationPrincipal User user) {
        comment.setUser(user);
        comment.setPost(postService.findById(id).orElse(null)); // Setuj post za komentar
        Comment savedComment = commentService.save(comment); // Implementiraj ovu metodu u servisu
        return ResponseEntity.ok(savedComment);
    }*/
}