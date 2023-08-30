package hu.csercsak_albert.lottery_app.analyser;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Ticket {

	private final int[] numbers;

	Ticket(int[] numbers) {
		this.numbers = numbers;
		Arrays.sort(this.numbers);
	}

	@Override
	public String toString() {
		return Arrays.stream(numbers).mapToObj(String::valueOf).collect(Collectors.joining(", "));
	}
}
