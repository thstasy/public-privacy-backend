package org.Stasy.PublicPrivacyAppBackendHeroku.subscription;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class SubscriptionConfirmationToken {

    @OneToOne(targetEntity = EmailEntity.class, fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "id")
    private EmailEntity emailEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public SubscriptionConfirmationToken() {
        // Default constructor
    }

    public SubscriptionConfirmationToken(EmailEntity emailEntity) {
        this.emailEntity = emailEntity;
        this.createdDate = new Date();
        this.confirmationToken = UUID.randomUUID().toString();
    }

    // Getters and setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public EmailEntity getEmailEntity() {
        return emailEntity;
    }

    public void setEmailEntity(EmailEntity emailEntity) {
        this.emailEntity = emailEntity;
    }

    @Override
    public String toString() {
        return "SubscriptionConfirmationToken{" +
                "id=" + id +
                ", confirmationToken='" + confirmationToken + '\'' +
                ", createdDate=" + createdDate +
                ", emailEntity=" + emailEntity +
                '}';
    }
}

