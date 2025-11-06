package org.example.passwordmanager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;

public class PostgresDatabase implements UserRepository {

    private final HikariDataSource ds;

    public PostgresDatabase(String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setPoolName("AppHikariPool");
        ds = new HikariDataSource(config);
    }

    @Override
    public boolean usernameExists(String email) {
        // SQL Injection Prevention
        // Validate and normalize email before use
        String safeEmail = SQLInjectionPrevention.validateEmail(email);

        String sql = "SELECT 1 FROM app_users WHERE email = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // SQL Injection Prevention
            ps.setString(1, safeEmail);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void saveUser(String email, String passwordHash) {
        String safeEmail = SQLInjectionPrevention.validateEmail(email);
        String safeHash = SQLInjectionPrevention.cleanText(passwordHash, 200);

        String sql = "INSERT INTO app_users(email, password_hash) VALUES(?, ?) " +
                "ON CONFLICT (email) DO UPDATE SET password_hash = EXCLUDED.password_hash";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // SQL Injection Prevention
            ps.setString(1, safeEmail);
            ps.setString(2, safeHash);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user", e);
        }
    }

    @Override
    public String findHashByEmail(String email) {
        // Validate email, SQL Injection Prevention
        String safeEmail = SQLInjectionPrevention.validateEmail(email);

        String sql = "SELECT password_hash FROM app_users WHERE email = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, safeEmail);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        if (ds != null) ds.close();
    }
}
