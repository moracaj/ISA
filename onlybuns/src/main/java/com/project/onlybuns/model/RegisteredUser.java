package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("registered_user")
public class RegisteredUser extends User {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // Add this line for comments

    @ManyToMany
    @JoinTable(
            name = "user_likes_post",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> likedPosts = new ArrayList<>(); // List of posts liked by the user

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "post-user") // Povezano sa `@JsonBackReference` u `Post` klasi
    private List<Post> posts = new ArrayList<>();

    @ManyToOne // Dodaj ovu vezu
    @JoinColumn(name = "admin_user_id") // Ovo će biti naziv kolone u tabeli RegisteredUser
    private AdminUser adminUser; // Referenca ka AdminUser






    // Getters and Setters for posts
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getNumberOfPosts() {
        return posts.size(); // Vraća broj postova
    }

    // Getters and Setters for likes








    // Getters and Setters for adminUser
    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }


    /*public int getFollowingCount() {
        return following.size();
    }*/
}
