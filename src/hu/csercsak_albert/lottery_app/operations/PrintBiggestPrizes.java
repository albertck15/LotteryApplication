package hu.csercsak_albert.lottery_app.operations;

import java.time.temporal.ChronoUnit;

import hu.csercsak_albert.lottery_app.data_service.Draw;
import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.general.FastQuitException;
import hu.csercsak_albert.lottery_app.main.UserInput;

class PrintBiggestPrizes extends AbstractOperation {

	private static final int MIN_COUNT = 1;
	private static final int MAX_COUNT = 10;

	PrintBiggestPrizes() {
		super("Print Biggest Prizes", "PBP");
	}

	@Override
	public void perform(UserInput userInput) throws DatabaseException, FastQuitException {
		printSeparatorLine();
		int count = 0;
		System.out.printf("%nHow many draws you want to query with the biggest prizes ? (%d-%d)%n", MIN_COUNT, MAX_COUNT);
		do {
			System.out.print(prompt);
			try {
				String input = userInput.getNext();
				if (input.equals(fastQuitChar)) {
					throw new FastQuitException();
				}
				count = Integer.parseInt(input);
			} catch (NumberFormatException e) {
			}
		} while (count < MIN_COUNT || count > MAX_COUNT);
		int i = 1;
		System.out.println();
		for (Draw draw : contactPoint.getDrawsOfHighestPrizes(count)) {
			print(i++, draw);
		}
		printSeparatorLine();
	}

	private void print(int count, Draw draw) throws DatabaseException {
		System.out.printf("The %d. biggest prize found in the last %d draw weeks was on %s%n", count, contactPoint.getDrawsCount(), draw.getDate());
		System.out.printf("and it was worth %,d Hungarian forints%n", draw.getFiveHitPrize());
		System.out.printf("at that time this was worth %,.0f €%n", contactPoint.calculateEuro(draw.getFiveHitPrize(), draw.getDate()));
		Draw drawBefore = contactPoint.getFiveHitDrawBefore(draw);
		long days = ChronoUnit.DAYS.between(drawBefore.getDate(), draw.getDate());
		System.out.printf("previous five hit was %d weeks before this.%n%n", days / 7);
	}
}
