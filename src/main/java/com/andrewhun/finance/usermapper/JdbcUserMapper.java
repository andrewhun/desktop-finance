package com.andrewhun.finance.usermapper;

import com.andrewhun.finance.database.DatabaseTemplate;
import com.andrewhun.finance.user.Password;
import com.andrewhun.finance.user.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class JdbcUserMapper implements UserMapper {

    private static final String FIND_BY_ID =
        "SELECT * FROM Users WHERE id = ?";
    private static final String FIND_BY_USERNAME =
        "SELECT * FROM Users WHERE username = ?";
    private static final String FIND_LOGGED_IN =
        "SELECT * FROM Users WHERE is_logged_in = 1";
    private static final String INSERT =
        "INSERT INTO Users (username, password_hash, balance, is_logged_in, hash_salt) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE =
        "UPDATE Users SET username = ?, password_hash = ?, balance = ?, is_logged_in = ?, hash_salt = ? WHERE id = ?";
    private static final String DELETE =
        "DELETE FROM Users WHERE id = ?";
    private static final String FIND_ALL_USERNAMES =
        "SELECT username FROM Users";

    private final DatabaseTemplate databaseTemplate;

    public JdbcUserMapper() {
        this.databaseTemplate = new DatabaseTemplate();
    }

    /** A unit of JDBC work whose {@link SQLException} is translated before it escapes. */
    @FunctionalInterface
    private interface JdbcCall<T> {
        T run() throws SQLException;
    }

    /**
     * Runs JDBC work and rewrites any {@link SQLException} as a {@link UserMapperException},
     * so no trace of the storage technology reaches the caller.
     */
    private <T> T translating(String operation, JdbcCall<T> call) {

        try {
            return call.run();
        }
        catch (SQLException e) {
            throw new UserMapperException("Could not " + operation, e);
        }
    }

    @Override
    public List<String> findAllUsernames() {
        return translating("list usernames", () ->
            databaseTemplate.queryMany(FIND_ALL_USERNAMES, rs -> rs.getString("username")));
    }

    @Override
    public Optional<User> findById(int id) {
        return translating("find user " + id, () ->
            databaseTemplate.queryOne(FIND_BY_ID, stmt -> stmt.setInt(1, id), this::mapRow));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return translating("find user '" + username + "'", () ->
            databaseTemplate.queryOne(FIND_BY_USERNAME, stmt -> stmt.setString(1, username), this::mapRow));
    }

    @Override
    public Optional<User> findLoggedIn() {
        return translating("find the logged-in user", () ->
            databaseTemplate.queryOne(FIND_LOGGED_IN, this::mapRow));
    }

    private User mapRow(ResultSet rs) throws SQLException {

        Password password = new Password(
            rs.getBytes("password_hash"),
            rs.getBytes("hash_salt")
        );
        return setUpUser(rs, password);
    }

    private User setUpUser(ResultSet rs, Password password) throws SQLException {

        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(password);
        user.setBalance(rs.getBigDecimal("balance"));
        user.setLoggedIn(rs.getInt("is_logged_in") == 1);
        return user;
    }

    @Override
    public void save(User user) {

        if (user.isNew()) {
            insert(user);
        }
        else {
            update(user);
        }
    }

    private void insert(User user) {

        int id = translating("save user '" + user.getUsername() + "'", () ->
            databaseTemplate.updateAndGetKey(INSERT, stmt -> bindUserParams(stmt, user)));
        if (id != -1) {
            user.setId(id);
        }
    }

    private void update(User user) {

        translating("save user '" + user.getUsername() + "'", () -> {
            databaseTemplate.update(UPDATE, stmt -> {
                bindUserParams(stmt, user);
                stmt.setInt(6, user.getId());
            });
            return null;
        });
    }

    private void bindUserParams(PreparedStatement stmt, User user) throws SQLException {

        stmt.setString(1, user.getUsername());
        stmt.setBytes(2, user.getPassword().getHash());
        stmt.setBigDecimal(3, user.getBalance());
        stmt.setInt(4, user.isLoggedIn() ? 1 : 0);
        stmt.setBytes(5, user.getPassword().getSalt());
    }

    @Override
    public void delete(int id) {

        translating("delete user " + id, () -> {
            databaseTemplate.update(DELETE, stmt -> stmt.setInt(1, id));
            return null;
        });
    }
}