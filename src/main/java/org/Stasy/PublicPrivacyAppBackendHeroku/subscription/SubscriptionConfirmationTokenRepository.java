package org.Stasy.PublicPrivacyAppBackendHeroku.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionConfirmationTokenRepository extends JpaRepository<SubscriptionConfirmationToken, Long> {
    SubscriptionConfirmationToken findByConfirmationToken(String confirmationToken);
}
