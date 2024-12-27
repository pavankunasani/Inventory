package org.application.daoconnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.application.Main;

public class DaoConnection {

	private static DaoConnection instance; // Singleton instance
	private static Connection connection; // Database connection

	// Private constructor to prevent instantiation
	private DaoConnection(String propertiesFilePath) {
		try {
//            Properties properties = new Properties();
//            FileInputStream input = new FileInputStream(propertiesFilePath);
//            properties.load(input);
//            String host = properties.getProperty("db.host");
//            String port = properties.getProperty("db.port");
//            String username = properties.getProperty("db.username");
//            String database = properties.getProperty("db.database");
//            String password = properties.getProperty("db.password");
			String host = "35.244.30.178";
			String port = "3306";
			String username = "udaydb";
			String database = "inventory";
			String password = "udaydb";
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
			DaoConnection.connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method to initialize the Singleton instance
	public static synchronized void initialize(String propertiesFilePath) {
		if (instance == null) {
			instance = new DaoConnection(propertiesFilePath);
		}
	}

	// Method to get the Singleton instance
	public static DaoConnection getInstance() {
		if (instance == null) {
			throw new IllegalStateException("DaoConnection has not been initialized. Call initialize() first.");
		}
		return instance;
	}

	// Provide a method to get the database connection
	public static Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			// throw new SQLException("Connection is closed. Please reinitialize the DAO.");
			// System.out.println(Main.propertiesFilePath + " prop file");
			initialize("");
		}
		return connection;
	}
}
