package hu.csercsak_albert.lottery_app.data_service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.main.Constants;

public class Draw {

	private static final LocalDate EUR_CALCULATE_MIN_DATE = LocalDate.parse(Constants.getSqlProperties().getProperty("min_eur_convert_date"));

	private final LocalDate date;
	private final int year;
	private final int week;

	private final int fiveHitCount;
	private final long fiveHitPrize;

	private final int fourHitCount;
	private final int fourHitPrize;

	private final int threeHitCount;
	private final int threeHitPrize;

	private final int twoHitCount;
	private final int twoHitPrize;

	private final Integer[] numbers;

	public Draw(ResultSet resultSet) throws SQLException { // Building up Draw object from database
		date = resultSet.getDate("draw_date").toLocalDate();
		year = resultSet.getInt("year");
		week = resultSet.getInt("week");
		fiveHitCount = resultSet.getInt("five_hit_count");
		fiveHitPrize = resultSet.getLong("five_hit_prize");
		fourHitCount = resultSet.getInt("four_hit_count");
		fourHitPrize = resultSet.getInt("four_hit_prize");
		threeHitCount = resultSet.getInt("three_hit_count");
		threeHitPrize = resultSet.getInt("three_hit_prize");
		twoHitCount = resultSet.getInt("two_hit_count");
		twoHitPrize = resultSet.getInt("two_hit_prize");
		numbers = (Integer[]) resultSet.getArray("drawn_numbers").getArray();
	}

	Draw(Matcher matcher, LocalDate date) { // Building up Draw object from the HTML data (Strings)
		this.date = date;
		this.year = Integer.parseInt(matcher.group("year"));
		this.week = Integer.parseInt(matcher.group("week"));
		this.fiveHitCount = Integer.parseInt(matcher.group("fiveHit"));
		this.fiveHitPrize = Long.parseLong(matcher.group("fiveHitPrize"));
		this.fourHitCount = Integer.parseInt(matcher.group("fourHit"));
		this.fourHitPrize = Integer.parseInt(matcher.group("fourHitPrize"));
		this.threeHitCount = Integer.parseInt(matcher.group("threeHit"));
		this.threeHitPrize = Integer.parseInt(matcher.group("threeHitPrize"));
		this.twoHitCount = Integer.parseInt(matcher.group("twoHit"));
		this.twoHitPrize = Integer.parseInt(matcher.group("twoHitPrize"));
		this.numbers = parseNumbers(matcher.group("drawnNumbers"));
	}

	/**********************************************************************************************
	 * Constructor helper method
	 */
	private Integer[] parseNumbers(String group) {
		Integer[] parsedNumber = new Integer[5];
		int i = 0;
		for (String s : group.split(" ")) {
			parsedNumber[i++] = Integer.parseInt(s);
		}
		return parsedNumber;
	}

	/**********************************************************************************************
	 * Getters
	 */

	public LocalDate getDate() {
		return date;
	}

	public int getYear() {
		return year;
	}

	public int getWeek() {
		return week;
	}

	public int getFiveHitCount() {
		return fiveHitCount;
	}

	public long getFiveHitPrize() {
		return fiveHitPrize;
	}

	public int getFourHitCount() {
		return fourHitCount;
	}

	public int getFourHitPrize() {
		return fourHitPrize;
	}

	public int getThreeHitCount() {
		return threeHitCount;
	}

	public int getThreeHitPrize() {
		return threeHitPrize;
	}

	public int getTwoHitCount() {
		return twoHitCount;
	}

	public int getTwoHitPrize() {
		return twoHitPrize;
	}

	public Integer[] getNumbers() {
		return numbers;
	}

	@Override
	public String toString() {
		try {
			double eurValue = 0;
			if (date.isAfter(EUR_CALCULATE_MIN_DATE)) {
				eurValue = DatabaseContactPointImpl.getInstance().calculateEuro(fiveHitPrize, date);
			}
			return "The draw was on %s, at the %d. week%nand there %s%nThe drawn numbers are : %s%n%n".formatted(date, week, //
					fiveHitPrize == 0 ? "was no five hit at that draw,"
							: "there was five hit at that draw which was worth %,d Forints %s"//
									.formatted(fiveHitPrize, eurValue == 0 ? ""//
											: "(%,.0f Euros at that time)".formatted(eurValue)),
					Stream.of(numbers).map(i -> i.toString()).collect(Collectors.joining("; ")));
		} catch (DatabaseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
