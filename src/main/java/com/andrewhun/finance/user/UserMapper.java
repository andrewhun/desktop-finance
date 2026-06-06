package com.andrewhun.finance.user;

import com.andrewhun.finance.database.DatabaseTemplate;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserMapper {

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

    public List<String> findAllUsernames() throws SQLException {
        return DatabaseTemplate.queryMany(FIND_ALL_USERNAMES, rs -> rs.getString("username"));
    }

    public Optional<User> findById(int id) throws SQLException {
        return DatabaseTemplate.queryOne(FIND_BY_ID, stmt -> stmt.setInt(1, id), this::mapRow);
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        return DatabaseTemplate.queryOne(FIND_BY_USERNAME, stmt -> stmt.setString(1, username), this::mapRow);
    }

    public Optional<User> findLoggedIn() throws SQLException {
        return DatabaseTemplate.queryOne(FIND_LOGGED_IN, this::mapRow);
    }

    private User mapRow(ResultSet rs) throws SQLException {
        Password password = Password.reconstitute(
            rs.getBytes("password_hash"),
            rs.getBytes("hash_salt")
        );
        return User.reconstitute(
            rs.getInt("id"),
            rs.getString("username"),
            password,
            rs.getBigDecimal("balance"),
            rs.getInt("is_logged_in") == 1
        );
    }

    public void save(User user) throws SQLException {
        if (user.isNew()) {
            insert(user);
        }
        else {
            update(user);
        }
    }

    private void insert(User user) throws SQLException {
        int id = DatabaseTemplate.updateAndGetKey(INSERT, stmt -> bindUserParams(stmt, user));
        if (id != -1) {
            user.id = id;
        }
    }

    private void update(User user) throws SQLException {
        DatabaseTemplate.update(UPDATE, stmt -> {
            bindUserParams(stmt, user);
            stmt.setInt(6, user.getId());
        });
    }

    private void bindUserParams(PreparedStatement stmt, User user) throws SQLException {
        stmt.setString(1, user.getUsername());
        stmt.setBytes(2, user.password.hash);
        stmt.setBigDecimal(3, user.getBalance());
        stmt.setInt(4, user.isLoggedIn() ? 1 : 0);
        stmt.setBytes(5, user.password.salt);
    }

    public void delete(int id) throws SQLException {
        DatabaseTemplate.update(DELETE, stmt -> stmt.setInt(1, id));
    }
}