package hu.csercsak_albert.lottery_app.operations;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.csercsak_albert.lottery_app.data_service.Draw;
import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.general.FastQuitException;
import hu.csercsak_albert.lottery_app.main.UserInput;

class ReadDraws extends AbstractOperation {

	private static final Pattern DATE_PATTERN = Pattern.compile("(?<year>\\d{4})(.|-| )(?<month>\\d{1,2})\\2(?<day>\\d{1,2})");

	ReadDraws() {
		super("Read draws", "RD");
	}

	@Override
	public void perform(UserInput userInput) throws DatabaseException, FastQuitException {
		printSeparatorLine();
		System.out.printf("%nWrite a date, or two to read interval accordingly%n");
		while (true) {
			System.out.print("-->");
			String input = userInput.getNext();
			if (input.equals(fastQuitChar)) {
				throw new FastQuitException();
			}
			Matcher m = DATE_PATTERN.matcher(input);
			SortedSet<LocalDate> dates = new TreeSet<>();
			while (m.find()) {
				try {
					dates.add(LocalDate.of(Integer.parseInt(m.group("year")), //
							Integer.parseInt(m.group("month")), //
							Integer.parseInt(m.group("day"))));
				} catch (DateTimeException e) {
					System.out.println("Wrong input!");
				}
			}
			System.out.println();
			if (dates.size() == 1) {
				printOneDraw(dates.first());
				printSeparatorLine();
				return;
			} else if (dates.size() == 2) {
				printInterval(dates);
				printSeparatorLine();
				return;
			}
		}
	}

	private void printOneDraw(LocalDate date) throws DatabaseException {
		while (true) {
			try {
				System.out.println(contactPoint.getDraw(date));
				break;
			} catch (IllegalArgumentException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
	}

	private void printInterval(SortedSet<LocalDate> dates) throws DatabaseException { // 2 sized set, contains from-to date values
		System.out.printf("Reading draws, please wait...%n%n");
		try {
			for (Draw draw : contactPoint.getDraws(dates.first(), dates.last())) {
				System.out.println(draw);
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
