package com.project.onlybuns.service;

import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.NotFoundPostException;
import com.project.onlybuns.model.User;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepo;
    private final UserRepository userRepo;

    @Autowired
    public PostService(PostRepository postRepo, UserRepository userRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepo.findById(postId);
    }

    public Post savePost(Post post) {
        return postRepo.save(post);
    }

    public void removePost(Long postId) {
        postRepo.deleteById(postId);
    }

    @Transactional
    public Post modifyPost(Post postData) {
        Post original = postRepo.findById(postData.getId())
                .orElseThrow(() -> new NotFoundPostException("Post not found with id: " + postData.getId()));

        if (postData.getImageUrl() == null || postData.getImageUrl().isEmpty()) {
            throw new IllegalArgumentException("Image URL must not be null or empty.");
        }
        if (postData.getDescription() == null) {
            throw new IllegalArgumentException("Description must not be null.");
        }

        original.setImageUrl(postData.getImageUrl());
        original.setDescription(postData.getDescription());

        return postRepo.save(original);
    }

    public List<Post> getPostsByUserId(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        return userOpt.map(postRepo::findByUser).orElse(Collections.emptyList());
    }

    public List<Post> fetchByUsername(String username) {
        return postRepo.findByUserUsername(username);
    }
}
