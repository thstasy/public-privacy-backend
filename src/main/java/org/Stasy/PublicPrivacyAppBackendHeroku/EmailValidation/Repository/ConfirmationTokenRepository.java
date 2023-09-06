package org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository;

import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    @Modifying
    @Query("DELETE FROM ConfirmationToken ct WHERE ct.user.id = ?1")
    void deleteByUserId(Long userId);
}
