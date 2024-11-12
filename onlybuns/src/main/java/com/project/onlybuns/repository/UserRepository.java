package com.project.onlybuns.repository;

import com.project.onlybuns.model.User;
import com.project.onlybuns.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //RegisteredUser findByUsername(String username);  // Metoda za pronalaženje korisnika po korisničkom imenu
    boolean existsByUsername(String username); // Proverava da li korisničko ime već postoji
   boolean existsByEmail(String email);

    List<User> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
            String firstName, String lastName, String email);
    List<User> findByPostsCountBetween(int min, int max);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    //RegisteredUser findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.userType = :userType")
    Optional<User> findRegisteredUserByUsername(@Param("username") String username, @Param("userType") UserType userType);

    // Optional<User> findRegisteredUserByUsername(String username, UserType userType); // Vraća Optional<RegisteredUser>
    List<User> findByUserType(UserType userType);
   // List<User>findByUserId(UserType userType);
}
