package com.example.energyconnectjavafx.DAO;

import com.example.energyconnectjavafx.DB.DBConnection;
import com.example.energyconnectjavafx.Model.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AdminDAO {

    public Admin findByEmailOrUsername(String input) throws SQLException {
        String sql = """
            SELECT ID, PASSWORD_HASH, IS_INITIALIZED
            FROM Admin
            WHERE EMAIL = ? OR USERNAME = ?
            LIMIT 1
        """;

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, input);
            ps.setString(2, input);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("ID"),
                        rs.getString("PASSWORD_HASH"),
                        rs.getBoolean("IS_INITIALIZED")
                );
            }
            return null;
        }
    }

    public void saveInviteToken(int adminId, String tokenHash, LocalDateTime expires) throws SQLException {
        String sql = """
            UPDATE Admin
            SET INVITE_TOKEN_HASH = ?, INVITE_TOKEN_EXPIRES = ?
            WHERE ID = ?
        """;
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ps.setTimestamp(2, Timestamp.valueOf(expires));
            ps.setInt(3, adminId);
            ps.executeUpdate();
        }
    }

    public Integer verifyInviteToken(String tokenHash) throws SQLException {
        String sql = """
            SELECT ID
            FROM Admin
            WHERE INVITE_TOKEN_HASH = ?
              AND IS_INITIALIZED = 0
              AND INVITE_TOKEN_EXPIRES > NOW()
        """;

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("ID") : null;
        }
    }

    public Admin findUninitializedByEmail(String email) throws SQLException {
        String sql = """
        SELECT ID
        FROM Admin
        WHERE EMAIL = ?
          AND IS_INITIALIZED = 0
        LIMIT 1
    """;
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? new Admin(rs.getInt("ID"), null, false) : null;
        }
    }

    public Admin findInitializedByEmailOrUsername(String input) throws SQLException {
        String sql = """
        SELECT ID, PASSWORD_HASH, IS_INITIALIZED
        FROM Admin
        WHERE (EMAIL = ? OR USERNAME = ?)
          AND IS_INITIALIZED = 1
        LIMIT 1
    """;
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, input);
            ps.setString(2, input);
            ResultSet rs = ps.executeQuery();
            return rs.next()
                    ? new Admin(rs.getInt("ID"),
                    rs.getString("PASSWORD_HASH"),
                    true)
                    : null;
        }
    }

    public void setCredentials(int adminId, String username, String password) throws SQLException {
        String sql = """
        UPDATE Admin
        SET USERNAME = ?, PASSWORD_HASH = ?, IS_INITIALIZED = 1
        WHERE ID = ?
    """;
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, com.example.energyconnectjavafx.util.PasswordUtil.hash(password));
            ps.setInt(3, adminId);
            ps.executeUpdate();
        }
    }

    public void saveResetToken(int adminId, String tokenHash) throws SQLException {
        String sql = """
        UPDATE Admin
        SET RESET_TOKEN_HASH = ?
        WHERE ID = ?
    """;
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ps.setInt(2, adminId);
            ps.executeUpdate();
        }
    }

    public void updatePassword(int adminId, String password) throws SQLException {
        String sql = """
        UPDATE Admin
        SET PASSWORD_HASH = ?
        WHERE ID = ?
    """;
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, com.example.energyconnectjavafx.util.PasswordUtil.hash(password));
            ps.setInt(2, adminId);
            ps.executeUpdate();
        }
    }

    public Integer verifyResetToken(String tokenHash) throws SQLException {
        String sql = """
        SELECT ID FROM Admin
        WHERE RESET_TOKEN_HASH = ?
    """;

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("ID") : null;
        }
    }
}
