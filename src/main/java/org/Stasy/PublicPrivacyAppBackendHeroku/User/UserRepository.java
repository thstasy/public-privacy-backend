package org.Stasy.PublicPrivacyAppBackendHeroku.User;

import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailAndPassword(String email, String password) throws UserNotFoundException;

    @Query(value = "SELECT u FROM User u WHERE u.resetToken = :resetToken")
    User findUserByResetToken(@Param("resetToken") String resetToken);

    User findByUsername(String username);

    User findByEmail(String email);
    @Query(value = "SELECT * FROM users u WHERE u.enabled = false AND COALESCE(u.created_at, '1970-01-01 00:00:00') < :inactiveSince", nativeQuery = true)
    List<User> findInactiveUsersBefore(@Param("inactiveSince") LocalDateTime inactiveSince);

    Optional<User> findOneByEmailAndPassword(String email, String password);

    User findByEmailIgnoreCase(String emailId);

    Boolean existsByEmail(String email);
}