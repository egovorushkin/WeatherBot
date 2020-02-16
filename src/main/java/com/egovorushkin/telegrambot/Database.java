package com.egovorushkin.telegrambot;

import java.sql.*;

/**
 * @author Evgenii Govorushkin
 * @version 1.0
 */

public class Database {
	private static final String URL = "jdbc:mysql://localhost:3306/weather_bot?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true\" +\n"
			+ "                    \"&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "123456789";

//    Connection connection = null;
//    PreparedStatement statement = null;
//    ResultSet resultSet = null;

	// Write data to DB
	public void writeToDb(Long chatId, String userName, String firstName, String lastName, Float latitude,
			Float lontitude) throws SQLException {
		boolean isUserExists = false;
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			PreparedStatement statement = connection
					.prepareStatement("SELECT chat_id FROM subscribers WHERE chat_id=?");
			statement.setLong(1, chatId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					isUserExists = true;
				}
				if (isUserExists) {
					System.out.println("User already exists.");

				} else {// adding new subscriber
					String sql = "INSERT INTO subscribers(chat_id, username, firstname, lastname, latitude, longitude) "
							+ "VALUES(?, ?, ?, ?, ?, ? ) ";
					statement = connection.prepareStatement(sql);
					statement.setLong(1, chatId);
					statement.setString(2, userName);
					statement.setString(3, firstName);
					statement.setString(4, lastName);
					statement.setFloat(5, latitude);
					statement.setFloat(6, lontitude);

					statement.executeUpdate();
					System.out.println("Added new subscriber.");
				}
			}
		}
	}

	// Read coordinates from DB (latitude)
	public Float readLatitude(Long chatId) throws SQLException {
		Float lat = null;
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			String sql = "SELECT latitude FROM subscribers WHERE chat_id = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, chatId);
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						lat = resultSet.getFloat("latitude");
					}
					return lat;
				}
			}
		}
	}

	// Read coordinates from DB (longitude)
	public Float readLongitude(Long chatId) throws SQLException {
		Float lat = null;
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

			String sql = "SELECT longitude FROM subscribers WHERE chat_id = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, chatId);
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						lat = resultSet.getFloat("longitude");
					}
					return lat;
				}
			}
		}
	}

	// Delete record from DB
	public void deleteFromDb(Long chatId) throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
			String sql = "DELETE FROM subscribers WHERE chat_id = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, chatId);
				statement.executeUpdate();
			}
		}
	}
}
