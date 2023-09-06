package org.Stasy.PublicPrivacyAppBackendHeroku.User;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {

///super important, the default constructor
    public User() {
    }

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Column(name="username",nullable=false)
    private String username;

    @Column(name="createdAt",nullable=false)
    private LocalDateTime createdAt;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String email;

    @Column(nullable = false)
    private boolean isEnabled;

    @Column(name = "reset_token")
    private String resetToken;


    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}