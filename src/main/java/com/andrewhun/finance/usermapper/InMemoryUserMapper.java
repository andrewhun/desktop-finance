package com.andrewhun.finance.usermapper;

import com.andrewhun.finance.user.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A {@link UserMapper} that keeps users in memory only — nothing is written to a real
 * database. Backs the in-memory storage mode (for demos or throwaway runs) and stands in
 * for the real database in tests that need working persistence semantics (generated ids,
 * unique usernames, round-tripping) without the cost or side effects of a live database.
 */
public class InMemoryUserMapper implements UserMapper {

    private final Map<Integer, User> usersById = new LinkedHashMap<>();
    private int nextId = 1;

    @Override
    public List<String> findAllUsernames() {

        List<String> usernames = new ArrayList<>();
        for (User user : usersById.values()) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(usersById.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findFirstMatching(user -> user.getUsername().equals(username));
    }

    @Override
    public Optional<User> findLoggedIn() {
        return findFirstMatching(User::isLoggedIn);
    }

    private Optional<User> findFirstMatching(Predicate<User> predicate) {
        return usersById.values().stream()
            .filter(predicate)
            .findFirst();
    }

    @Override
    public void save(User user) {

        if (user.isNew()) {
            insert(user);
        }
        else {
            usersById.put(user.getId(), user);
        }
    }

    private void insert(User user) {

        if (userAlreadyExists(user)) {
            throw new UserMapperException(
                "A user with username '" + user.getUsername() + "' already exists");
        }
        user.setId(nextId++);
        usersById.put(user.getId(), user);
    }

    private Boolean userAlreadyExists(User user) {
        Optional<User> optionalUser = findByUsername(user.getUsername());
        return optionalUser.isPresent();
    }

    @Override
    public void delete(int id) {
        usersById.remove(id);
    }
}