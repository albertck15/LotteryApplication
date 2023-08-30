package hu.csercsak_albert.lottery_app.general;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 5297706439226104752L;

	public DatabaseException() {
	}

	public DatabaseException(String message) {
		super(message);
	}
}
