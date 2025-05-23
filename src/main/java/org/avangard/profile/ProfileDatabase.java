package org.avangard.profile;

import org.avangard.util.DataBase;
import java.sql.*;

public class ProfileDatabase extends DataBase {
    public ProfileDatabase(String jdbcUrl, String userName, String password) {
        super(jdbcUrl, userName, password);
    }

    public void setGroup(long id, Group group, Date expireTime) {
        String update = "UPDATE profiles SET user_group = ?, expire_time = ? WHERE uuid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, group.name());
            stmt.setDate(2, expireTime);
            stmt.setLong(3, id);
            if (stmt.executeUpdate() == 0) {
                saveProfile(new Profile(id, group, expireTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Profile getProfile(long id) {
        String query = "SELECT user_group, expire_time FROM profiles WHERE uuid = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    try {
                        Group group = Group.valueOf(rs.getString("user_group"));
                        return new Profile(id, group, rs.getDate("expire_time"));
                    } catch (IllegalArgumentException e) {
                        return new Profile(id, Group.DEFAULT, rs.getDate("expire_time"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Profile newProfile = new Profile(id, Group.DEFAULT, null);
        saveProfile(newProfile);
        return newProfile;
    }

    public void saveProfile(Profile profile) {
        String insertOrUpdate = "INSERT INTO profiles (uuid, user_group, expire_time) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE user_group = VALUES(user_group), expire_time = VALUES(expire_time)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertOrUpdate)) {
            stmt.setLong(1, profile.getUuid());
            stmt.setString(2, profile.getGroup().name());
            stmt.setDate(3, profile.getExpireTime());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
