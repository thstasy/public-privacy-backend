package org.Stasy.PublicPrivacyAppBackendHeroku.User;

import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserAlreadyExistsException;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserServiceInterface extends JpaRepository<User, Long>{


    User findUserByUsername(String username) throws UserAlreadyExistsException, UserNotFoundException;

    User findUserByEmail(String email)throws UserNotFoundException;

    void delete(User entity);

}