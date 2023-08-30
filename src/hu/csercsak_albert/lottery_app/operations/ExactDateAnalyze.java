package hu.csercsak_albert.lottery_app.operations;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.general.FastQuitException;
import hu.csercsak_albert.lottery_app.main.UserInput;

class ExactDateAnalyze extends AbstractOperation {

	ExactDateAnalyze() {
		super("Analyze an exact date", "EDT");
	}

	@Override
	public void perform(UserInput userInput) throws DatabaseException, FastQuitException {
		System.out.println("Coming soon...");
	}

}
