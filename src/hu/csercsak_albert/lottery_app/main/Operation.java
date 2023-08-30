package hu.csercsak_albert.lottery_app.main;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.general.FastQuitException;

public interface Operation {

	void perform(UserInput userInput) throws DatabaseException, FastQuitException;

	String getName();

	String getShortName();
}
