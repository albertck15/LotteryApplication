package hu.csercsak_albert.lottery_app.general;

public class FastQuitException extends Exception {

	private static final long serialVersionUID = 8098484381672376517L;

	public FastQuitException() {
	}

	public FastQuitException(String message) {
		super(message);
	}
}
