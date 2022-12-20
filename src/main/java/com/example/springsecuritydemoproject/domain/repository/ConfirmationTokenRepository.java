package com.example.springsecuritydemoproject.domain.repository;

import com.example.springsecuritydemoproject.domain.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    @Query("SELECT ct FROM ConfirmationToken ct WHERE ct.token = :token")
    Optional<ConfirmationToken> findByToken(@Param("token") String token);

}
