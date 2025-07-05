package com.example.usermanagement.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    public User() {

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    private String role;

    public String getRole() {
        return role;
    }

    public @NotBlank(message = "Name is required") String getName() {
        return name;
    }

    public @NotBlank(message = "Email is required") @Email(message = "Email format is invalid") String getEmail() {
        return email;
    }

    public void setName(@NotBlank(message = "Name is required") String name) {
        this.name = name;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Email format is invalid") String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }
}