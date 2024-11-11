/*package com.project.onlybuns.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;


@Entity
public class RegisteredUser extends User {

    //private int followerCount;

   // @OneToMany(mappedBy = "registeredUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JsonManagedReference(value = "post-user"
    private List<Post> posts;

    // Getteri i setteri
   // public int getFollowerCount() { return followerCount; }
   // public void setFollowerCount(int followerCount) { this.followerCount = followerCount; }

    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
}*/
