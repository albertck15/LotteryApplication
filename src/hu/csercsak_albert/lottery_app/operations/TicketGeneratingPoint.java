package hu.csercsak_albert.lottery_app.operations;

import java.util.List;

import hu.csercsak_albert.lottery_app.analyser.Ticket;

public interface TicketGeneratingPoint {

	List<Ticket> getTickets();
}
