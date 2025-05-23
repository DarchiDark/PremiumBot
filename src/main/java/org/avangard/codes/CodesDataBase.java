package org.avangard.codes;

import org.avangard.util.DataBase;
import java.sql.*;

public class CodesDataBase extends DataBase {
    public CodesDataBase(String jdbcUrl, String userName, String password) {
        super(jdbcUrl, userName, password);
    }

    public void decrementActivations(String id) {
        String query = "UPDATE codes SET activations = activations - 1 WHERE id = ? AND activations > 0";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCode(Code code) {
        String query = "INSERT INTO codes (id, code, unlimited, activations, permission, time, permission_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, code.getId());
            stmt.setString(2, code.getCode());
            stmt.setBoolean(3, code.isUnlimited());
            stmt.setInt(4, code.getActivations());
            stmt.setString(5, code.getPermission());
            stmt.setDate(6, Date.valueOf(code.getTime()));
            stmt.setLong(7, code.getPermissionTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCode(String id) {
        String query = "DELETE FROM codes WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Code getCode(String codeValue) {
        String query = "SELECT * FROM codes WHERE code = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, codeValue);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Code(
                            rs.getString("id"),
                            rs.getString("code"),
                            rs.getBoolean("unlimited"),
                            rs.getInt("activations"),
                            rs.getString("permission"),
                            rs.getDate("time").toLocalDate(),
                            rs.getLong("permission_time")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
