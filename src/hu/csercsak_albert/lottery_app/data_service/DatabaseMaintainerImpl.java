package hu.csercsak_albert.lottery_app.data_service;

import java.sql.SQLException;
import java.time.LocalDate;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.main.DatabaseMaintainer;

public class DatabaseMaintainerImpl implements DatabaseMaintainer {

	private final DatabaseCommunactionPoint dbCommunicationPoint;

	public DatabaseMaintainerImpl() throws SQLException {
		dbCommunicationPoint = DatabaseCommunactionPoint.getInstance();
	}

	@Override
	public void checkAndUpdate() throws DatabaseException {
		if (LocalDate.now().isAfter(dbCommunicationPoint.getNextUpdate())) {
			System.out.printf("%nUpdating database, please wait.%n%n");
			new DatabaseUpdater().update();
		}
	}
}
