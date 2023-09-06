package org.Stasy.PublicPrivacyAppBackendHeroku.Opinion;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "opinion") // Change to "production" when needed
public class Opinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opinion_id", nullable = false)
    private Long id;

    @Column(name = "collaborator_name", nullable = false)
    private String collaboratorName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String body;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "country")
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "city")
    private String city;
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    //this is super important, default opinion constructor
    public Opinion() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCollaboratorName() {
        return collaboratorName;
    }

    public void setCollaboratorName(String collaboratorName) {
        this.collaboratorName = collaboratorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if (body != null && !body.isEmpty()) {
            String[] words = body.trim().split("\\s+");
            if (words.length > 500) {
                StringBuilder truncatedBody = new StringBuilder();
                for (int i = 0; i <= 500; i++) {
                    truncatedBody.append(words[i]).append(" ");
                }
                body = truncatedBody.toString().trim();
            }
        }
        this.body = body;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }


    public String findCollaboratorNameById(Integer id) {
        return collaboratorName;
    }


}