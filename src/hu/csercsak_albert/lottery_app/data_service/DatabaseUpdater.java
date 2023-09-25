package hu.csercsak_albert.lottery_app.data_service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.general.HtmlReadingException;

class DatabaseUpdater {

	private final DatabaseCommunactionPoint dbCommunicationPoint = DatabaseCommunactionPoint.getInstance();
	private final LocalDate nextUpdateDate;

	DatabaseUpdater() {
		nextUpdateDate = dbCommunicationPoint.getNextUpdate();
	}

	/**********************************************************************************************
	 * Updating draws
	 */

	void update() throws DatabaseException {
		try {
			updateDraws();
			updateEurHufExchanges();
			dbCommunicationPoint.setProperties();
		} catch (SQLException e) {
			System.err.printf("ERROR! (%s) Updating database has been failed!", e.getMessage());
		} catch (IOException e) {
			System.err.printf("ERROR! (%s) Updating files has been failed!", e.getMessage());
		}

	}

	private void updateDraws() throws SQLException, IOException {
		try (var connection = DriverManager.getConnection(dbCommunicationPoint.getConnection(), dbCommunicationPoint.getLogin(),
				dbCommunicationPoint.getPassword())) {
			var reader = new HTMLReader();
			List<Draw> draws = reader.readDrawsBackUntil(nextUpdateDate);
			String query = """
					INSERT INTO draws (year, week, draw_date, five_hit_count, five_hit_prize,
									 four_hit_count, four_hit_prize, three_hit_count,
									 three_hit_prize, two_hit_count, two_hit_prize, drawn_numbers)
					VALUES	(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
					""";
			for (var draw : draws) {
				try {
					executeNext(connection, query, draw);
				} catch (SQLException e) { //<-- throws when trying to insert a value that is already in
					break;
				}
			}
		}
	}

	private void executeNext(Connection connection, String query, Draw draw) throws SQLException {
		try (var preStatement = connection.prepareStatement(query)) {
			preStatement.setInt(1, draw.getYear());
			preStatement.setInt(2, draw.getWeek());
			preStatement.setDate(3, new java.sql.Date(draw.getDate().toEpochDay() * 86400000L));
			preStatement.setInt(4, draw.getFiveHitCount());
			preStatement.setLong(5, draw.getFiveHitPrize());
			preStatement.setInt(6, draw.getFourHitCount());
			preStatement.setInt(7, draw.getFourHitPrize());
			preStatement.setInt(8, draw.getThreeHitCount());
			preStatement.setInt(9, draw.getThreeHitPrize());
			preStatement.setInt(10, draw.getTwoHitCount());
			preStatement.setInt(11, draw.getTwoHitPrize());
			preStatement.setArray(12, connection.createArrayOf("INTEGER", draw.getNumbers()));
			preStatement.executeUpdate();
		}
	}

	/**********************************************************************************************
	 * Updating exchange values
	 */

	private void updateEurHufExchanges() throws SQLException {
		List<LocalDate> dates = getDatesToUpdate();
		HTMLReader reader = new HTMLReader();
		try (var connection = DriverManager.getConnection(dbCommunicationPoint.getConnection(), dbCommunicationPoint.getLogin(),
				dbCommunicationPoint.getPassword())) {
			for (var date : dates) {
				if (date.isAfter(dbCommunicationPoint.getMinEurReadDate())) {// <- Only have data from 2009-10-10
					String url = String.format("https://www.exchangerates.org.uk/EUR-HUF-%s_%s_%s-exchange-rate-history.html", date.getDayOfMonth(),
							date.getMonth(), date.getYear());
					try {
						double oneEuroValueToHuf = reader.getValueFromURL(url);
						String query = "INSERT INTO eur_huf_exchange_rates (date, huf_value) VALUES(?,?);";
						try (var preStatement = connection.prepareStatement(query)) {
							preStatement.setDate(1, new java.sql.Date(date.toEpochDay() * 86400000L));
							preStatement.setDouble(2, oneEuroValueToHuf);
							preStatement.executeUpdate();
						}
					} catch (HtmlReadingException e) {
						throw new SQLException("Something went wrong while updating Eur/Huf values");
					}
				}
			}
		}
	}

	private List<LocalDate> getDatesToUpdate() throws SQLException {
		List<LocalDate> dates = new ArrayList<>();
		try (var connection = DriverManager.getConnection(dbCommunicationPoint.getConnection(), dbCommunicationPoint.getLogin(),
				dbCommunicationPoint.getPassword())) {
			try (var statement = connection.createStatement()) {
				String query = "SELECT draw_date FROM draws WHERE draw_date NOT IN (SELECT date FROM eur_huf_exchange_rates);";
				try (var resultSet = statement.executeQuery(query)) {
					while (resultSet.next()) {
						dates.add(resultSet.getDate("draw_date").toLocalDate());
					}
				}
			}
		}
		return dates;
	}

}
