package hu.csercsak_albert.lottery_app.operations;

import java.util.SortedMap;

import hu.csercsak_albert.lottery_app.general.DatabaseException;

public interface AnalyserContactPoint {

	TicketGeneratingPoint getTicketGenerator() throws DatabaseException;

	SortedMap<Integer, Integer> getFrequencies() throws DatabaseException;
}
