package com.example.springsecuritydemoproject.domain.entity;

import com.example.springsecuritydemoproject.utils.security.roles.ApplicationUserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"password", "bio", "image"})
@Getter
@Setter
@Entity(name = "User")
@Table(
        name = "user_details",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "user_username_unique", columnNames = "username")
        }
)
public class UserEntity {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "username", nullable = false, columnDefinition = "TEXT")
    private String username;
    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    @Column(name = "image")
    private String image;

    @Column(
            name = "created_at",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private ZonedDateTime createdAt;
    @Column(
            name = "updated_at",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private ZonedDateTime updatedAt;

    @Enumerated(value = EnumType.STRING)
    private ApplicationUserRole role;

}
