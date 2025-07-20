package com.project.onlybuns.controller;

import com.project.onlybuns.model.Comment;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.User;
import com.project.onlybuns.service.CommentService;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/post/{postId}")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<Comment> createComment(
            @PathVariable Long postId,
            @RequestBody Comment commentInput) {

        // Preuzimanje korisnika iz SecurityContext-a
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        // Pronalazak posta
        Post post = postService.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build(); // Post ne postoji
        }

        // Kreiranje novog komentara
        Comment comment = new Comment();
        comment.setContent(commentInput.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentService.createOrUpdateComment(comment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.removeComment(id);
        return ResponseEntity.noContent().build();
    }
}
