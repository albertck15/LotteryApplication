import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import hu.csercsak_albert.lottery_app.data_service.DatabaseMaintainerImpl;

class Setup {

	private static final Properties SQL_PROPERTIES = new Properties();
	private static final String SQL_PROP_PATH = "src/hu/csercsak_albert/lottery_app/resources/sql.properties";
	static {
		try (var fos = new FileInputStream(SQL_PROP_PATH)) {
			SQL_PROPERTIES.load(fos);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new RuntimeException();
		}
	}

	public static void main(String[] args) {
		new Setup().run();
	}

	private void run() {
		System.out.println("""
				Welcome to the database setupper,
				This program will setup the needed tables for the Lottery Application
				Please wait...

				""");
		try {
			setupTable1();
			setupTable2();
			updateDraws();
			System.out.println("Setting up database has been successful!");
		} catch (SQLException e) {
			System.err.printf("Setting up database has been failed! (%s)%n", e.getMessage());
		}
		System.out.printf("%nGoodbye!%n");
	}

	private void setupTable1() throws SQLException {
		try (var connection = DriverManager.getConnection(SQL_PROPERTIES.getProperty("connection"), SQL_PROPERTIES.getProperty("username"),
				SQL_PROPERTIES.getProperty("password"))) {
			String query = """
					CREATE TABLE draws
					(draw_date DATE PRIMARY KEY,
					year INTEGER,
					week INTEGER,
					five_hit_count INTEGER,
					five_hit_prize BIGINT,
					four_hit_count INTEGER,
					four_hit_prize INTEGER,
					three_hit_count INTEGER,
					three_hit_prize INTEGER,
					two_hit_count INTEGER,
					two_hit_prize INTEGER,
					drawn_numbers INTEGER[]
					);
										""";
			try (var statement = connection.createStatement()) {
				statement.executeUpdate(query);
			}
		}
	}

	private void setupTable2() throws SQLException {
		try (var connection = DriverManager.getConnection(SQL_PROPERTIES.getProperty("connection"), SQL_PROPERTIES.getProperty("username"),
				SQL_PROPERTIES.getProperty("password"))) {
			String query = """
					CREATE TABLE eur_huf_exchange_rates
					(date DATE PRIMARY KEY, huf_value REAL);""";
			try (var statement = connection.createStatement()) {
				statement.executeUpdate(query);
			}
		}
	}
	private void updateDraws() throws SQLException {
		new DatabaseMaintainerImpl();
	}

}
