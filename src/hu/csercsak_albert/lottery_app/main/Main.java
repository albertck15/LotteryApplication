package hu.csercsak_albert.lottery_app.main;

import java.sql.SQLException;

import hu.csercsak_albert.lottery_app.data_service.DatabaseMaintainerImpl;
import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.general.DefinitonException;
import hu.csercsak_albert.lottery_app.general.FastQuitException;
import hu.csercsak_albert.lottery_app.menu.MenuGetter;

public class Main {

	public static void main(String[] args) throws DefinitonException {
		new Main().run();
	}

	private void run() throws DefinitonException {
		var th = new TextHolder();
		try {
			th.welcome();
			checkAndUpdateDatabase();
			th.printInfos();
			process();
		} catch (DatabaseException e) {
			System.out.printf("%s.%n%s%n", e.getMessage(), ", the program stops.");
		}
		th.farewell();
	}

	private void checkAndUpdateDatabase() throws DatabaseException {
		try {
			DatabaseMaintainer maintainer = new DatabaseMaintainerImpl();
			maintainer.checkAndUpdate();
		} catch (SQLException e) {
			throw new DatabaseException("ERROR! Something went wrong while accessing the database maintainer");
		}
	}

	private void process() throws DefinitonException {
		try (UserInput userInput = new UserInput()) {
			Menu menu = MenuGetter.getInstance().getMenu(userInput);
			for (Operation op; (op = menu.printAndChoose()) != null;) {
				try {
					op.perform(userInput);
				} catch (DatabaseException e) {
					System.out.printf("%n%s%n", e.getMessage());
				}
			}
		} catch (FastQuitException e) {
		}
	}
}
