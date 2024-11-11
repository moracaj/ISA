package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-comments")  // Sprečava serijalizaciju povezane RegisteredUser
    private User user; // Povezivanje sa RegisteredUser

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference(value = "post-comments")// Sprečava serijalizaciju povezane Post
    private Post post; // Povezivanje sa Post

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdTime;  // Datum i vreme kada je komentar napravljen

    public LocalDateTime getCreatedAt() {
        return createdTime;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdTime = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();  // Postavljanje trenutnog vremena pre nego što komentar bude sačuvan
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
