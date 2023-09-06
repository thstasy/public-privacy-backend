package org.Stasy.PublicPrivacyAppBackendHeroku.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    //List<EmailEntity> findAllEmails();
    boolean existsByEmail(String email);

    EmailEntity findByEmailIgnoreCase(String email);
}