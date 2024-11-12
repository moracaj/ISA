package com.project.onlybuns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  //  @NotBlank(message = "Username cannot be blank")
   // @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

   // @NotBlank(message = "Password cannot be blank")
   // @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

   // @Email(message = "Email should be valid")
   // @NotBlank(message = "Email cannot be blank")
    private String email;


    //@Column(name = "first_name")
   // @NotBlank(message = "First name cannot be blank")
    private String firstName;

    //@Column(name = "last_name")
   // @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    private String address;

    //private boolean isActive = false; // Podrazumevano nije aktiviran

    @Column(name = "posts_count")
    private Integer postsCount = 0; // Broj objava

   // @Column(name = "followers_count")
   // private Integer followersCount = 0; // Broj pratilaca  //////////////ovo mislim da mi ne treba

   // @Column(name = "likes_count")
   // private Integer likesCount = 0; // Broj lajkova  //////////////ni ovo

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    // Konstruktor bez parametara
    public User() {
    }

    // Konstruktor sa parametrima
    public User(String username, String password, String email, String firstName, String lastName, String address, UserType userType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.userType = userType;
    }

    // Getteri i setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPostsCount() {
        return postsCount;
    }

    public void setPostsCount(Integer postsCount) {
        this.postsCount = postsCount;
    }

   /* public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }*/

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JsonManagedReference(value = "post-user"
    private List<Post> posts;

    // Getteri i setteri
    // public int getFollowerCount() { return followerCount; }
    // public void setFollowerCount(int followerCount) { this.followerCount = followerCount; }

    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }

}
