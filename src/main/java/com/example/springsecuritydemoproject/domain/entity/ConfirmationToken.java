package com.example.springsecuritydemoproject.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ConfirmationToken")
@Table(name = "confirmation_token")
public class ConfirmationToken {

    @Id
    @SequenceGenerator(name = "confirmation_token_sequence", sequenceName = "confirmation_token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_token_sequence")
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
            nullable = false,
            name = "app_user_id",
            foreignKey = @ForeignKey(name = "userEntity_confirmationToken_fk")
    )
    private UserEntity userEntity;

}