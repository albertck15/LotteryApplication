package hu.csercsak_albert.lottery_app.data_service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.main.Constants;
import hu.csercsak_albert.lottery_app.operations.DatabaseContactPoint;

public class DatabaseContactPointImpl implements DatabaseContactPoint {

	private static final DatabaseContactPointImpl INSTANCE = new DatabaseContactPointImpl();

	private DatabaseContactPointImpl() {
	}

	public static DatabaseContactPointImpl getInstance() {
		return INSTANCE;
	}

	private static final Map<LocalDate, Draw> CACHE = new LinkedHashMap<>() {

		private static final int MAX_SIZE = 150;

		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Entry<LocalDate, Draw> eldest) {
			return size() > MAX_SIZE;
		}
	};

	private static final DatabaseCommunactionPoint DB_COMMUNICATION_POINT = DatabaseCommunactionPoint.getInstance();

	private List<Draw> allDraws = new ArrayList<>();

	private int drawsCount;

	private static final LocalDate MIN_DATE = LocalDate.parse(Constants.getSqlProperties().getProperty("min_date"));
	private static final LocalDate MAX_DATE = LocalDate.parse(Constants.getSqlProperties().getProperty("max_date"));

	private Connection getConnection() throws SQLException { // INFO ez biztonságos ?
		return DriverManager.getConnection(DB_COMMUNICATION_POINT.getConnection(), DB_COMMUNICATION_POINT.getLogin(),
				DB_COMMUNICATION_POINT.getPassword());
	}

	/**********************************************************************************************
	 * Database query methods
	 */

	@Override
	public Draw getDraw(LocalDate date) throws DatabaseException {
		if (date.isBefore(MIN_DATE) || date.isAfter(MAX_DATE)) {
			throw new IllegalArgumentException("This date is out of the database's bounds");
		}
		if (date.getDayOfWeek() != DayOfWeek.SATURDAY) {
			System.out.printf("%nThis date is not falls on saturday! Finding next...%n%n"); // BUG : Some draws was on Sundays(2023.01.01)
		}
		Draw draw = CACHE.remove(date);
		if (draw == null) {
			draw = queryDraw(date);
		}
		CACHE.put(date, draw);
		return draw;
	}

	private Draw queryDraw(LocalDate date) throws DatabaseException {
		try (var connection = getConnection()) {
			String query = "SELECT * FROM draws WHERE draw_date >= ? ORDER BY draw_Date LIMIT 1;"; //FIXME Doesn't work for intervals
			try (var statement = connection.prepareStatement(query)) {
				statement.setDate(1, java.sql.Date.valueOf(date));
				try (var resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return new Draw(resultSet);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new DatabaseException("Something went wrong while trying to read draw");
	}

	@Override
	public List<Draw> getDraws(LocalDate from, LocalDate to) throws DatabaseException {
		if (from.isBefore(MIN_DATE) || from.isAfter(MAX_DATE) || to.isBefore(MIN_DATE) || to.isAfter(MAX_DATE)) {
			throw new IllegalArgumentException("This date is out of the database's bounds");
		}
		List<Draw> draws = new ArrayList<>();
		while (from.getDayOfWeek() != DayOfWeek.SATURDAY) {
			from = from.plusDays(1);
		}
		while (to.getDayOfWeek() != DayOfWeek.SATURDAY) {
			to = to.minusDays(1);
		}
		if (from.isBefore(to)) {
			for (; from.isBefore(to); from = from.plusDays(7)) {
				draws.add(getDraw(from));
			}
		} else {
			for (; to.isBefore(from); to = to.plusDays(7)) {
				draws.add(getDraw(to));
			}
		}
		return draws;
	}

	@Override
	public List<Draw> getDrawsOfHighestPrizes(int count) throws DatabaseException {
		List<Draw> draws = new ArrayList<>();
		try (var connection = getConnection()) {
			String query = "SELECT * FROM draws ORDER BY five_hit_prize DESC LIMIT ?;";
			try (var preStatement = connection.prepareStatement(query)) {
				preStatement.setInt(1, count);
				try (var resultSet = preStatement.executeQuery()) {
					while (resultSet.next()) {
						draws.add(new Draw(resultSet));
					}
					return draws;
				}
			}
		} catch (SQLException e) {
		}
		throw new DatabaseException("Something went wrong while trying to read biggest prize");
	}

	@Override
	public Draw getFiveHitDrawBefore(Draw draw) throws DatabaseException {
		try (var connection = getConnection()) {
			String query = "SELECT * FROM draws WHERE draw_date < ? AND five_hit_prize != 0 ORDER BY draw_date DESC LIMIT 1;";
			try (var statement = connection.prepareStatement(query)) {
				statement.setDate(1, java.sql.Date.valueOf(draw.getDate()));
				try (var resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return new Draw(resultSet);
					}
				}
			}
		} catch (SQLException e) {
		}
		throw new DatabaseException("Querying a specific draw has been failed.");
	}

	@Override
	public List<Draw> getAllDraw() throws DatabaseException {
		if (allDraws.isEmpty()) {
			queryAllDraw();
		}
		return allDraws;
	}

	private void queryAllDraw() throws DatabaseException {
		try (var connection = getConnection()) {
			String query = "SELECT * FROM draws";
			try (var statement = connection.createStatement()) {
				try (var resultSet = statement.executeQuery(query)) {
					while (resultSet.next()) {
						allDraws.add(new Draw(resultSet));
					}
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException("Querying all draw has been failed! (%s)".formatted(e.getMessage()));
		}
	}

	/**********************************************************************************************
	 * Sizes and min-max dates of the draws in the database
	 */

	@Override
	public int getDrawsCount() throws DatabaseException {
		if (drawsCount == 0) {
			drawsCount = getNumberOfDraws();
		}
		return drawsCount;
	}

	private int getNumberOfDraws() throws DatabaseException {
		try (var connection = getConnection()) {
			String query = "SELECT COUNT(*) FROM draws;";
			try (var statement = connection.createStatement()) {
				try (var resultSet = statement.executeQuery(query)) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
		}
		throw new DatabaseException("Something went wrong while trying to read draw");
	}

	@Override
	public double calculateEuro(long prize, LocalDate date) throws DatabaseException {
		try (var connection = getConnection()) {
			String query = "SELECT huf_value FROM eur_huf_exchange_rates WHERE date <= ? LIMIT 1;";
			try (var statement = connection.prepareStatement(query)) {
				statement.setDate(1, java.sql.Date.valueOf(date));
				try (var resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return prize / resultSet.getDouble(1);
					}
				}
			}
		} catch (SQLException e) {
		}
		throw new DatabaseException("Something went wrong while trying to read euro value");
	}

	@Override
	public LocalDate getOldestDrawDate() throws DatabaseException {
		try (var connection = getConnection()) {
			String query = "SELECT * FROM draws ORDER BY draw_date LIMIT 1;";
			try (var statement = connection.prepareStatement(query)) {
				try (var resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getDate("draw_date").toLocalDate();
					}
				}
			}
		} catch (SQLException e) {
		}
		throw new DatabaseException("Something went wrong while reading minimum date");
	}

	@Override
	public LocalDate getLatestDrawDate() throws DatabaseException {
		try (var connection = getConnection()) {
			String query = "SELECT * FROM draws ORDER BY draw_date DESC LIMIT 1;";
			try (var statement = connection.prepareStatement(query)) {
				try (var resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getDate("draw_date").toLocalDate();
					}
				}
			}
		} catch (SQLException e) {
		}
		throw new DatabaseException("Something went wrong while reading maximum date");
	}
}
