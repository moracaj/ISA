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
    boolean userExistsByUsername(String username);
    boolean userExistsByEmail(String email);

    List<User> searchUsersByNameOrEmail(String firstName, String lastName, String email);

    List<User> findUsersWithPostRange(int min, int max);

    Optional<User> lookupByUsername(String username);

    Optional<User> lookupByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.userType = :userType")
    Optional<User> getRegisteredUserByUsername(@Param("username") String username,
                                               @Param("userType") UserType userType);

    List<User> getUsersByType(UserType userType);
}
