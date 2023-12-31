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
		LocalDate nextUpdate = dbCommunicationPoint.getNextUpdate();
		if (nextUpdate == null || LocalDate.now().isAfter(nextUpdate)) { // 1970.01.01 at first start
			System.out.printf("%nUpdating database, please wait.%n%n");
			new DatabaseUpdater().update();
		}
	}
}
