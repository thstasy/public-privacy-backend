package org.Stasy.PublicPrivacyAppBackendHeroku.subscription;

import jakarta.persistence.*;

@Entity
@Table(name = "EmailEntity") // Set the table name here
public class EmailEntity {
    EmailEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private boolean enabled; // New attribute

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
