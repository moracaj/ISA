package com.project.onlybuns.controller;

import com.project.onlybuns.dto.PostDto;
import com.project.onlybuns.model.Comment;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.CommentService;
import com.project.onlybuns.service.ImageUploadService;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final ImageUploadService imageUploadService;

    @Autowired
    public ContentController(PostService postService, CommentService commentService,
                             UserService userService, ImageUploadService imageUploadService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }

    // POST kreiranje objave sa slikom
    @PostMapping("/create")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<Post> publishNewPost(
            @RequestParam("description") String description,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("location") String location,
            @RequestPart("imageFile") MultipartFile imageFile) {

        String username = getCurrentUsername();
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String uploadedImagePath;
        try {
            uploadedImagePath = imageUploadService.uploadImage(imageFile);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImageUrl(uploadedImagePath);
        newPost.setUser(optionalUser.get());
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setLatitude(latitude);
        newPost.setLongitude(longitude);
        newPost.setLocation(location);

        Post saved = postService.save(newPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET svi postovi + komentari
    @GetMapping("/all")
    public List<Map<String, Object>> fetchAllPosts() {
        List<Post> allPosts = postService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (Post post : allPosts) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", Optional.ofNullable(post.getUser()).map(User::getUsername).orElse("Unknown"));
            postData.put("countLikes", post.getLikesCount());
            postData.put("createdAt", post.getCreatedAt() != null ? post.getCreatedAt().format(formatter) : "N/A");

            List<Map<String, Object>> commentDataList = new ArrayList<>();
            for (Comment c : post.getComments()) {
                Map<String, Object> cMap = new HashMap<>();
                cMap.put("id", c.getId());
                cMap.put("content", c.getContent());
                cMap.put("username", Optional.ofNullable(c.getUser()).map(User::getUsername).orElse("Anonymous"));
                cMap.put("createdAt", c.getCreatedAt() != null ? c.getCreatedAt().format(formatter) : "N/A");
                commentDataList.add(cMap);
            }

            postData.put("comments", commentDataList);
            response.add(postData);
        }

        return response;
    }

    // GET - Svi postovi samo od korisnika
    @GetMapping("/my")
    @PreAuthorize("hasRole('REGISTERED')")
    public List<Map<String, Object>> getMyPosts() {
        String username = getCurrentUsername();
        List<Post> userPosts = postService.getPostsByUsername(username);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Post post : userPosts) {
            Map<String, Object> postData = new HashMap<>();
            postData.put("id", post.getId());
            postData.put("imageUrl", post.getImageUrl());
            postData.put("description", post.getDescription());
            postData.put("username", Optional.ofNullable(post.getUser()).map(User::getUsername).orElse("Unknown"));
            postData.put("countLikes", post.getLikesCount());

            List<Map<String, Object>> commentDataList = new ArrayList<>();
            for (Comment c : post.getComments()) {
                Map<String, Object> cMap = new HashMap<>();
                cMap.put("id", c.getId());
                cMap.put("content", c.getContent());
                cMap.put("username", Optional.ofNullable(c.getUser()).map(User::getUsername).orElse("Anonymous"));
                commentDataList.add(cMap);
            }

            postData.put("comments", commentDataList);
            response.add(postData);
        }

        return response;
    }

    // GET - Preuzimanje slike
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("src/main/resources/static.images/" + filename);
            Resource fileResource = new UrlResource(filePath.toUri());

            if (fileResource.exists() && fileResource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(fileResource);
            }
            return ResponseEntity.notFound().build();
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Jedan post po ID-u
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostDetails(@PathVariable Long postId) {
        return postService.findById(postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
