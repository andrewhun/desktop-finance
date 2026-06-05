package com.andrewhun.finance.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseTemplate {

    @FunctionalInterface
    public interface StatementBinder {
        void bind(PreparedStatement stmt) throws SQLException;
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    public static <T> Optional<T> queryOne(String sql, StatementBinder binder, RowMapper<T> mapper)
            throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            binder.bind(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapper.map(rs)) : Optional.empty();
            }
        }
    }

    public static <T> Optional<T> queryOne(String sql, RowMapper<T> mapper) throws SQLException {
        return queryOne(sql, stmt -> {}, mapper);
    }

    public static <T> List<T> queryMany(String sql, StatementBinder binder, RowMapper<T> mapper)
            throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            binder.bind(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
                return results;
            }
        }
    }

    public static <T> List<T> queryMany(String sql, RowMapper<T> mapper) throws SQLException {
        return queryMany(sql, stmt -> {}, mapper);
    }

    public static void update(String sql, StatementBinder binder) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            binder.bind(stmt);
            stmt.executeUpdate();
        }
    }

    public static int updateAndGetKey(String sql, StatementBinder binder) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            binder.bind(stmt);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public static void execute(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}