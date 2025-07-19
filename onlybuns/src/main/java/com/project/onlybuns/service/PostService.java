package com.project.onlybuns.service;

import com.project.onlybuns.dto.PostDto;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.NotFoundPostException;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.repository.PostRepository;
import com.project.onlybuns.repository.RegisteredUserRepository;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
//import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }





//    public long countPostsForWeek() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime startOfWeek = now.minus(1, ChronoUnit.WEEKS);
//        return postRepository.findPostsByDateRange(startOfWeek, now).size();
//    }
//
//
//
//
//    public long countPostsForMonth() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime startOfMonth = now.minus(1, ChronoUnit.MONTHS);
//        return postRepository.findPostsByDateRange(startOfMonth, now).size();
//    }
//
//    public long countPostsForYear() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime startOfYear = now.minus(1, ChronoUnit.YEARS);
//        return postRepository.findPostsByDateRange(startOfYear, now).size();
//    }



    @Transactional
    public Post update(Post updatedPost) {
        // Proveri da li post sa datim ID-om postoji
        Post existingPost = postRepository.findById(updatedPost.getId())
                .orElseThrow(() -> new NotFoundPostException("Post not found with id: " + updatedPost.getId()));

        // Proveri da li su svi potrebni podaci ispravni pre ažuriranja
        if (updatedPost.getImageUrl() == null || updatedPost.getImageUrl().isEmpty()) {
            throw new IllegalArgumentException("Image URL must not be null or empty.");
        }
        if (updatedPost.getDescription() == null) {
            throw new IllegalArgumentException("Description must not be null.");
        }

        // Ažuriraj atribute postojeće objave
        existingPost.setImageUrl(updatedPost.getImageUrl());
        existingPost.setDescription(updatedPost.getDescription());

        // Sačuvaj ažuriranu objavu u bazi
        return postRepository.save(existingPost);
    }

    public void delete(Long id) {
        postRepository.deleteById(id); // Ova metoda ne treba da vraća ništa
    }

    @Autowired
    private RegisteredUserRepository registeredUserRepository; // Dodajte ovo

    public List<Post> findByUserId(Long userId) {
        RegisteredUser user = registeredUserRepository.findById(userId)
                .orElse(null); // Pronađite korisnika po ID-ju

        if (user != null) {
            return postRepository.findByUser(user); // Koristite korisnika da dobijete postove
        }
        return Collections.emptyList(); // Vratite praznu listu ako korisnik nije pronađen
    }

    public List<Post> getPostsByUsername(String username) {
        // Pretpostavljam da postoji metoda koja povlači postove na osnovu username-a
        return postRepository.findByUserUsername(username);
    }


    @org.springframework.cache.annotation.Cacheable("Post")
    public int getTotalPostsCount() {
        return postRepository.findAll().size();
    }
    @org.springframework.cache.annotation.Cacheable("Post")
    public List<Post> findPostsFromLastMonth() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return postRepository.findPostsAfterDate(thirtyDaysAgo);
    }



//    public List<PostDto> getPostsLocations() {
//        List<Post> posts = postRepository.findAll();
//        return posts.stream()
//                .filter(post -> post.getLatitude() != null && post.getLongitude() != null)
//                .map(post -> new PostDto(
//                        post.getImageUrl(),
//                        post.getLatitude(),
//                        post.getLongitude(),
//                        post.getDescription()))
//                .toList();
//    }
public List<PostDto> getPostsLocations() {
    List<Post> posts = postRepository.findAllWithCoordinates();
    return posts.stream()
            .map(post -> new PostDto(
                    post.getImageUrl(),
                    post.getLatitude(),
                    post.getLongitude(),
                    post.getDescription()))
            .toList();
}



}

