package hu.csercsak_albert.lottery_app.analyser;

import java.util.SortedMap;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.operations.AnalyserContactPoint;
import hu.csercsak_albert.lottery_app.operations.TicketGeneratingPoint;

public class AnalyserContactPointImpl implements AnalyserContactPoint {

	private static final Analyser ANALYSER = Analyser.getInstance();

	@Override
	public TicketGeneratingPoint getTicketGenerator() throws DatabaseException {
		return new TicketGeneratingPointImpl();
	}

	@Override
	public SortedMap<Integer, Integer> getFrequencies() throws DatabaseException {
		return ANALYSER.getFrequencies();
	}

}
