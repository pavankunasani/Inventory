package org.application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OutWardDaoImpl implements IOutWardDao {

	private Connection getConnection() throws SQLException {
		String host = "35.244.30.178";
		String port = "3306";
		String username = "udaydb";
		String database = "inventory";
		String password = "udaydb";
		String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
		return DriverManager.getConnection(url, username, password);

	}

}
