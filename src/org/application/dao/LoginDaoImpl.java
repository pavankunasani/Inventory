package org.application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.application.daoconnection.DaoConnection;
import org.mindrot.jbcrypt.BCrypt;

public class LoginDaoImpl implements ILoginDao {

	private Connection getConnection() throws SQLException {
        return DaoConnection.getConnection(); // Ensure this returns a connection
    }
    public boolean userLogin(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        String storedHashedPassword = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username); // Only use the username in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    storedHashedPassword = rs.getString("password");
                }
            }

            // Check the hashed password using BCrypt
            if (storedHashedPassword != null) {
                return BCrypt.checkpw(password, storedHashedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly in production code
        }
        return false; // Return false if there was an error or user not found
    }


}
