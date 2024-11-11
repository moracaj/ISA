package com.project.onlybuns.repository;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAll();  // Pronalazak svih objava
    //List<Post> findByIsDeletedFalse();
    List<Post> findByUserUsername(String username);

    @Query("SELECT p.user.username FROM Post p WHERE p.user.id = :userId")
    Optional<String> findUsernameByUserId(Long userId);

    List<Post> findByUser(User user); // Preporučeno

}
