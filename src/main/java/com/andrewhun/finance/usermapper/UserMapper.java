package com.andrewhun.finance.usermapper;

import com.andrewhun.finance.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Persistence operations for {@link User} aggregates.
 *
 * <p>Callers work through this interface and never name a concrete mapper; the one place
 * that does is {@link UserMapperFactory}, which builds the implementation matching the
 * storage mode selected at startup.
 *
 * <p>No method here declares a storage-specific exception. Implementations report failure
 * as {@link UserMapperException}, which keeps the choice of backend invisible to callers.
 */
public interface UserMapper {

    List<String> findAllUsernames();

    Optional<User> findById(int id);

    Optional<User> findByUsername(String username);

    Optional<User> findLoggedIn();

    /**
     * Inserts a new user or updates an existing one.
     *
     * @throws UserMapperException if the username is already taken, or the write fails
     */
    void save(User user);

    void delete(int id);
}
