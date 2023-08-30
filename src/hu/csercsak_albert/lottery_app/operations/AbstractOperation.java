package hu.csercsak_albert.lottery_app.operations;

import hu.csercsak_albert.lottery_app.data_service.DatabaseContactPointImpl;
import hu.csercsak_albert.lottery_app.main.Constants;
import hu.csercsak_albert.lottery_app.main.Operation;

public abstract class AbstractOperation implements Operation {

	protected final DatabaseContactPoint contactPoint = DatabaseContactPointImpl.getInstance();
	protected final String fastQuitChar = Constants.getMenuProperties().getProperty("fast_quit_char");

	private final String fullName;
	private final String shortName;
	protected final String prompt;

	AbstractOperation(String fullName, String shortName) {
		this.fullName = fullName;
		this.shortName = shortName;
		prompt = Constants.getMenuProperties().getProperty("prompt");
	}

	@Override
	public String getName() {
		return fullName;
	}

	@Override
	public String getShortName() {
		return shortName;
	}

	void printSeparatorLine() {
		System.out.println(String.valueOf('\u1397').repeat(40));
	}
}
