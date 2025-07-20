package com.project.onlybuns.repository;

import com.project.onlybuns.model.RegisteredUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    Optional<RegisteredUser> findByUsername(String username);

    @Query("SELECT u FROM RegisteredUser u WHERE u.username = :username")
    RegisteredUser findByUsername2(@Param("username") String username);

    List<RegisteredUser> findByLastActiveDateBefore(LocalDateTime date);

    List<RegisteredUser> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email);

    List<RegisteredUser> findByPostsCountBetween(int min, int max);

    // Dodavanje paginacije
    Page<RegisteredUser> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email, Pageable pageable);

    Page<RegisteredUser> findByPostsCountBetween(int min, int max, Pageable pageable);

    // Broj korisnika sa objavama

    // Count users who have comments but no posts (users who made only comments)
    @Query("SELECT COUNT(u) FROM RegisteredUser u WHERE u.posts IS EMPTY AND EXISTS (SELECT c FROM Comment c WHERE c.user = u)")
    long countByCommentsIsNotNullAndPostsIsEmpty();

    // Count users who have neither posts nor comments (no activity)
    @Query("SELECT COUNT(u) FROM RegisteredUser u WHERE u.posts IS EMPTY AND NOT EXISTS (SELECT c FROM Comment c WHERE c.user = u)")
    long countUsersWithNoActivity();

    @Query("SELECT COUNT(u) FROM RegisteredUser u WHERE u.posts IS NOT EMPTY")
    long countByPostsIsNotNull();
    @Query("SELECT COUNT(u) FROM RegisteredUser u")
    long countAllUsers();


    @Query("SELECT COUNT(DISTINCT u) FROM RegisteredUser u " +
            "JOIN u.posts p " +
            "LEFT JOIN u.comments c " +
            "WHERE p.createdAt BETWEEN :startDate AND :endDate " +
            "AND (c.createdAt IS NULL OR c.createdAt NOT BETWEEN :startDate AND :endDate)") // Isključuje korisnike koji su komentarisali u tom periodu
    long countUsersWithPostsOnlyInDateRange(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT u) FROM RegisteredUser u " +
            "JOIN u.comments c " +
            "LEFT JOIN u.posts p " +
            "WHERE c.createdAt BETWEEN :startDate AND :endDate " +
            "AND (p.createdAt IS NULL OR p.createdAt NOT BETWEEN :startDate AND :endDate)") // Isključuje korisnike koji su napravili postove
    long countUsersWithCommentsOnlyInDateRange(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);


    @Query("SELECT COUNT(DISTINCT u) FROM RegisteredUser u " +
            "LEFT JOIN u.posts p ON p.createdAt BETWEEN :startDate AND :endDate " +
            "LEFT JOIN u.comments c ON c.createdAt BETWEEN :startDate AND :endDate " +
            "WHERE (p.id IS NULL AND c.id IS NULL)")  // Korisnici koji nemaju postove ni komentare u tom periodu
    long countUsersWithNoActivityAndDateRange(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);


    @Query("SELECT COUNT(DISTINCT u) FROM RegisteredUser u " +
            "JOIN u.posts p " +
            "JOIN u.comments c " +
            "WHERE p.createdAt BETWEEN :startDate AND :endDate " +
            "AND c.createdAt BETWEEN :startDate AND :endDate")
    long countUsersWithPostsAndCommentsInDateRange(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);



}

