package com.project.onlybuns.controller;

import com.project.onlybuns.config.JwtAuthenticationFilter;

import com.project.onlybuns.dto.PostDto;

import com.project.onlybuns.model.*;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import com.project.onlybuns.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts") // Base path for post-related endpoints
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final AdMessageService adMessageService;
    private  final RegisteredUserRepository registeredUserRepository;

   // @Autowired
    private final PostRepository postRepository;

    private final UserService userService;

    private final ImageUploadService imageUploadService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, UserService userService,ImageUploadService imageUploadService, RegisteredUserRepository registeredUserRepository, AdMessageService adMessageService, PostRepository postRepository) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
        this.registeredUserRepository = registeredUserRepository;
        this.adMessageService = adMessageService;
        this.postRepository = postRepository;
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

    @PostMapping("/create")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<Post> createPost(
            @RequestParam("description") String description,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("location") String location,
            @RequestPart("imageFile") MultipartFile imageFile) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisteredUser loggedUser = userService.findByUsername1(username).orElse(null);

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

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<String> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisteredUser loggedUser = userService.findByUsername1(username).orElse(null);

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
    }



//    @GetMapping("/stats")
//    public Map<String, Long> getPostStats() {
//        Map<String, Long> stats = new HashMap<>();
//        stats.put("weekly", postService.countPostsForWeek());
//        stats.put("monthly", postService.countPostsForMonth());
//        stats.put("yearly", postService.countPostsForYear());
//        return stats;
//    }


    @GetMapping("/allPosts")
    public List<Map<String, Object>> getAllPosts(@AuthenticationPrincipal UserProfile userDetails) {
        // Dobavljanje svih objava
        List<Post> allPosts = postService.findAll();

        // Lista koja će sadržati sortirane objave
        List<Post> sortedPosts;

        // Provera da li je korisnik ulogovan
        if (userDetails != null) {
            // Ako je korisnik ulogovan, dobavljamo informacije o korisniku
            String loggedInUsername = userDetails.getUsername();
            RegisteredUser loggedInUser = registeredUserRepository.findByUsername(loggedInUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Logged-in user not found"));





            // Kombinovanje objava
            sortedPosts = new ArrayList<>();

        } else {
            // Ako korisnik nije ulogovan, postovi se ne sortiraju
            sortedPosts = allPosts;
        }

        // Format za datum i vreme
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        // Priprema rezultata za front-end
        List<Map<String, Object>> postsWithUsernamesAndComments = new ArrayList<>();
        for (Post post : sortedPosts) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", post.getUser() != null ? post.getUser().getUsername() : "Unknown");

            String formattedDate = post.getCreatedAt() != null ? post.getCreatedAt().format(formatter) : "Unknown date";
            postData.put("createdAt", formattedDate);

            List<Map<String, Object>> commentsData = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("id", comment.getId());
                commentData.put("content", comment.getContent());
                commentData.put("username", comment.getUser() != null ? comment.getUser().getUsername() : "Unknown");
                String formattedCommentDate = comment.getCreatedAt() != null ? comment.getCreatedAt().format(formatter) : "Unknown date";
                commentData.put("createdAt", formattedCommentDate);
                commentsData.add(commentData);
            }
            postData.put("comments", commentsData);

            postsWithUsernamesAndComments.add(postData);
        }

        return postsWithUsernamesAndComments;
    }


    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long id, @RequestBody Comment comment, @AuthenticationPrincipal RegisteredUser user) {
        comment.setUser(user);
        comment.setPost(postService.findById(id).orElse(null)); // Setuj post za komentar
        Comment savedComment = commentService.save(comment); // Implementiraj ovu metodu u servisu
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/totalPosts")
    public ResponseEntity<Map<String, Integer>> getTotalPostsCount() {
        // Dohvatanje svih registrovanih korisnika

        // Izračunavanje ukupnog broja objava
        int totalPosts = postService.getTotalPostsCount();

        // Priprema odgovora
        Map<String, Integer> response = new HashMap<>();
        response.put("totalPosts", totalPosts);

        return ResponseEntity.ok(response);
    }




    @GetMapping("/locations")
    public ResponseEntity<List<PostDto>> getPostsLocations() {
        List<PostDto> locations = postService.getPostsLocations();
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/promote/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> promotePost(@PathVariable Long postId) {
        Post post = postService.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        adMessageService.sendAdPost(
                post.getUser().getUsername(),
                post.getDescription(),
                post.getCreatedAt()
        );

        return ResponseEntity.ok("Promotional message sent!");
    }

    @GetMapping("/nearby")
    public List<PostDto> getNearbyPosts(@RequestParam("lat") double lat, @RequestParam("lon") double  lon) {
        System.out.println("👉 POZVAN JE getNearbyPosts() sa: lat=" + lat + ", lon=" + lon);

        double radiusInKm = 10.0;

        List<User> nearbyUsers = registeredUserRepository.findAll().stream()
                .filter(u -> distance(lat, lon, u.getLatitude(), u.getLongitude()) <= radiusInKm)
                .collect(Collectors.toList());

        List<Post> posts = new ArrayList<>();
        for (User user : nearbyUsers) {
            if (user instanceof RegisteredUser registeredUser) {
                posts.addAll(postRepository.findByUser(registeredUser));
            }
           // posts.addAll(postRepository.findByUser((RegisteredUser) user));
        }

        return posts.stream()
                .map(post -> new PostDto(
                        post.getImageUrl(),
                        post.getUser().getLatitude(),
                        post.getUser().getLongitude(),
                        post.getDescription()

                ))
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}