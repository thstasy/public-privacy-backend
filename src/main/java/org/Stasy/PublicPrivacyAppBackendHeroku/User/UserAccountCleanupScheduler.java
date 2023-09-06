package org.Stasy.PublicPrivacyAppBackendHeroku.User;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class UserAccountCleanupScheduler {

    private final UserServiceImpl userServiceImpl;

    public UserAccountCleanupScheduler(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Scheduled(fixedRate = 600000) // Run every ten minutes (10 minutes = 600000 milliseconds)
    public void deleteInactiveUsers() {
        LocalDateTime thresholdTime = LocalDateTime.now().minus(Duration.ofMinutes(10));
        userServiceImpl.deleteInactiveUsers(thresholdTime);
    }

}
