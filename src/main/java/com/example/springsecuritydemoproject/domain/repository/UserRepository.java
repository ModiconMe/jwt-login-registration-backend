package com.example.springsecuritydemoproject.domain.repository;

import com.example.springsecuritydemoproject.domain.entity.UserEntity;
import com.example.springsecuritydemoproject.utils.security.roles.ApplicationUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username")String username);

    @Query("SELECT u FROM User u " +
            "WHERE " +
            "(:username = NULL OR u.username = :username) AND " +
            "(:role = NULL OR u.role = :role)")
    List<UserEntity> findByFilter(@Param("username") String username, @Param("role") ApplicationUserRole role);

}
