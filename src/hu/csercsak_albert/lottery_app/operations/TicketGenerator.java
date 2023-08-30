package hu.csercsak_albert.lottery_app.operations;

import java.util.List;

import hu.csercsak_albert.lottery_app.analyser.Ticket;
import hu.csercsak_albert.lottery_app.analyser.TicketGeneratingPointImpl;
import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.main.UserInput;

class TicketGenerator extends AbstractOperation {

	private final TicketGeneratingPoint generator;

	TicketGenerator() {
		super("Generate tickets", "TG");
		try {
			generator = new TicketGeneratingPointImpl();
		} catch (DatabaseException e) {
			System.err.println("ERROR! %s the application can not start!".formatted(e.getMessage()));
			throw new RuntimeException();
		}
	}

	@Override
	public void perform(UserInput userInput) throws DatabaseException {
		List<Ticket> tickets = generator.getTickets();
		int i = 1;
		for (Ticket ticket : tickets) {
			System.out.printf("%d. szelvény : %s%n", i, ticket);
		}
	}
}
