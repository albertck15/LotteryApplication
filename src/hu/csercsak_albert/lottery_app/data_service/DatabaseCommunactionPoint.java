package hu.csercsak_albert.lottery_app.data_service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.main.Constants;

class DatabaseCommunactionPoint {

	private static final DatabaseCommunactionPoint INSTANCE = new DatabaseCommunactionPoint();

	private DatabaseCommunactionPoint() {
	}

	static DatabaseCommunactionPoint getInstance() {
		return INSTANCE;
	}

	private static final Properties PROPERTIES = Constants.getSqlProperties();
	private static final LocalDate NEXT_UPDATE = LocalDate.parse(PROPERTIES.getProperty("next_update"));
	private static final String LOGIN = PROPERTIES.getProperty("username");
	private static final String PASSWORD = PROPERTIES.getProperty("password");
	private static final String CONNECTION = PROPERTIES.getProperty("connection");
	private static final LocalDate MIN_EUR_READ_DATE = LocalDate.parse(PROPERTIES.getProperty("min_eur_convert_date"));
	private static final LocalDate MIN_DRAW_DATE = LocalDate.parse(PROPERTIES.getProperty("min_date"));

	LocalDate getNextUpdate() {
		return NEXT_UPDATE;
	}

	String getLogin() {
		return LOGIN;
	}

	String getPassword() {
		return PASSWORD;
	}

	String getConnection() {
		return CONNECTION;
	}

	LocalDate getMinEurReadDate() {
		return MIN_EUR_READ_DATE;
	}

	void setProperties() throws DatabaseException { // Saving next update's date into the properties file
		LocalDate next = DatabaseContactPointImpl.getInstance().getEarliestDate().plusDays(8);
		PROPERTIES.setProperty("next_update", getAsFormatted(next));
		PROPERTIES.setProperty("max_date", getAsFormatted(next.minusDays(8))); // This will be the newest date in the database
		LocalDate minDate = DatabaseContactPointImpl.getInstance().getOldestDrawDate();
		if (MIN_DRAW_DATE.isBefore(minDate)) { // <-- replacing default value at first start
			PROPERTIES.setProperty("min_date", getAsFormatted(minDate));
		}
		try (FileOutputStream outputStream = new FileOutputStream(Constants.getSqlPropertiesPath())) {
			PROPERTIES.store(outputStream, "Updated properties");
		} catch (IOException e) {
			System.err.println("ERROR! " + e.getMessage());
		}
	}

	private String getAsFormatted(LocalDate date) {
		return "%s-%s-%s".formatted("" + date.getYear(), //
				date.getMonthValue() < 10 ? "0%d".formatted(date.getMonthValue()) : "" + date.getMonthValue(), //
				date.getDayOfMonth() < 10 ? "0%d".formatted(date.getDayOfMonth()) : "" + date.getDayOfMonth());
	}
}
