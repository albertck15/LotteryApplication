package hu.csercsak_albert.lottery_app.main;

import hu.csercsak_albert.lottery_app.general.DatabaseException;

public interface DatabaseMaintainer {

	void checkAndUpdate() throws DatabaseException;
}
