package org.avangard.profile;

import org.avangard.util.DataBase;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReferralDataBase extends DataBase {
    public ReferralDataBase(String jdbcUrl, String userName, String password) {
        super(jdbcUrl, userName, password);
    }

    public void incrementActivations(String id, String presenter) {
        String query = """
        INSERT INTO referral_activators (id, referral)
        SELECT ?, ? FROM dual
        WHERE NOT EXISTS (SELECT 1 FROM referral_activators WHERE id = ?)
          AND EXISTS (SELECT 1 FROM referrals WHERE presenter = ?);
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            long longId = Long.parseLong(id);
            stmt.setLong(1, longId);
            stmt.setString(2, presenter);
            stmt.setLong(3, longId);
            stmt.setString(4, presenter);
            if (stmt.executeUpdate() > 0) {
                try (PreparedStatement update = conn.prepareStatement(
                        "UPDATE referrals SET activations = activations + 1 WHERE presenter = ?")) {
                    update.setString(1, presenter);
                    update.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(String presenter) {
        String query = "INSERT INTO referrals (presenter, activations) VALUES (?, 0)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, presenter);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(String presenter) {
        try (Connection conn = getConnection();
             PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM referral_activators WHERE referral = ?");
             PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM referrals WHERE presenter = ?")) {
            stmt1.setString(1, presenter);
            stmt1.executeUpdate();
            stmt2.setString(1, presenter);
            stmt2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int statistics(String presenter) {
        String query = "SELECT activations FROM referrals WHERE presenter = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, presenter);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("activations");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, Integer> topReferrals(int count) {
        Map<String, Integer> top = new LinkedHashMap<>();
        String query = "SELECT presenter, activations FROM referrals ORDER BY activations DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, count);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    top.put(rs.getString("presenter"), rs.getInt("activations"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return top;
    }

}
