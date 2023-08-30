package hu.csercsak_albert.lottery_app.analyser;

import java.util.SortedMap;
import java.util.TreeMap;

import hu.csercsak_albert.lottery_app.data_service.DatabaseContactPointImpl;
import hu.csercsak_albert.lottery_app.data_service.Draw;
import hu.csercsak_albert.lottery_app.general.DatabaseException;

class Analyser {

	private static final Analyser INSTANCE = new Analyser();

	public static Analyser getInstance() {
		return INSTANCE;
	}

	private Analyser() {
	}

	private final SortedMap<Integer, Integer> frequencies = new TreeMap<>();

	SortedMap<Integer, Integer> getFrequencies() throws DatabaseException {
		if (frequencies.isEmpty()) {
			calculateFrequencies();
		}
		return frequencies;
	}

	private void calculateFrequencies() throws DatabaseException {
		for (Draw draw : DatabaseContactPointImpl.getInstance().getAllDraw()) {
			for (Integer n : draw.getNumbers()) {
				frequencies.put(n, frequencies.getOrDefault(n, 0) + 1);
			}
		}
	}
}
