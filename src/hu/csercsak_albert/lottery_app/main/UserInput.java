package hu.csercsak_albert.lottery_app.main;

import java.util.Scanner;

public class UserInput implements AutoCloseable {

	private final Scanner scanner;

	UserInput() {
		scanner = new Scanner(System.in);
	}

	public String getNext() {
		return scanner.nextLine().trim();
	}

	@Override
	public void close() {
		scanner.close();
	}

}
