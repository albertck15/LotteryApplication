package hu.csercsak_albert.lottery_app.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import hu.csercsak_albert.lottery_app.analyser.AnalyserContactPointImpl;
import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.main.UserInput;

class AnalysisPrinter extends AbstractOperation {

	private final AnalyserContactPoint analyser;

	private final int numberDrawn;

	AnalysisPrinter() {
		super("Print Analysis", "PA");
		analyser = new AnalyserContactPointImpl();
		try {
			numberDrawn = contactPoint.getDrawsCount() * 5; // 5 numbers per draw
		} catch (DatabaseException e) {
			System.err.println("ERROR! %s the application can not start!".formatted(e.getMessage()));
			throw new RuntimeException();
		}
	}

	@Override
	public void perform(UserInput userInput) throws DatabaseException {
		SortedMap<Integer, Integer> frequencies = analyser.getFrequencies();
		List<Entry<Integer, Integer>> frequenciesSortedByVal = new ArrayList<>(frequencies.entrySet());
		frequenciesSortedByVal.sort(Entry.comparingByValue()); // Sorting by values
		printSeparatorLine();
		System.out.printf("There is %d. numbers%n%n", numberDrawn);
		System.out.println("Distribution numbers:");
		printRelativeFrequencies(frequencies);
		printCommonNumbers(frequenciesSortedByVal);
		printLeastCommonNumbers(frequenciesSortedByVal);
		printSeparatorLine();
	}

	private void printRelativeFrequencies(Map<Integer, Integer> frequencies) throws DatabaseException {
		System.out.printf("%6s %19s%n%n", "Number", "Relative frequency");
		for (Entry<Integer, Integer> entry : frequencies.entrySet()) {
			System.out.printf("%6d %18.2f%%%n", entry.getKey(), entry.getValue() * 100d / numberDrawn);
		}
	}

	private void printCommonNumbers(List<Entry<Integer, Integer>> frequenciesSortedByVal) {
		var list = new ArrayList<>(frequenciesSortedByVal.subList(frequenciesSortedByVal.size() - 15, frequenciesSortedByVal.size()));
		Collections.reverse(list);
		System.out.printf("%nThe most-commonly drawn numbers are :%n%n");
		for (Entry<Integer, Integer> entry : list) {
			System.out.printf("%d with %.2f%% chance%n", entry.getKey(), entry.getValue() * 100d / numberDrawn);
		}
	}

	private void printLeastCommonNumbers(List<Entry<Integer, Integer>> frequenciesSortedByVal) {
		System.out.printf("%nThe least commonly drawn numbers are :%n%n");
		var list = new ArrayList<>(frequenciesSortedByVal.subList(0, 15));
		Collections.reverse(list);
		for (Entry<Integer, Integer> entry : list) {
			System.out.printf("%d with %.2f%%%n", entry.getKey(), entry.getValue() * 100d / numberDrawn);
		}
	}
}
