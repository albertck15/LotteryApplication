package hu.csercsak_albert.lottery_app.analyser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.operations.TicketGeneratingPoint;

public class TicketGeneratingPointImpl implements TicketGeneratingPoint {

	private final Random random;

	private final List<Entry<Integer, Integer>> frequencies;

	public TicketGeneratingPointImpl() throws DatabaseException {
		random = new Random();
		frequencies = new ArrayList<>(Analyser.getInstance().getFrequencies().entrySet());
		frequencies.sort(Entry.comparingByValue()); // Sorting by values
	}

	@Override
	public List<Ticket> getTickets() {
		List<Ticket> tickets = new ArrayList<>(generateByMostFrequentNumbers());
		tickets.addAll(generateByLeastFrequentNumbers());
		return tickets;
	}

	private List<Ticket> generateByMostFrequentNumbers() {
		List<Ticket> tickets = new ArrayList<>();
		List<Integer> frequentNumbers = new ArrayList<>();
		for (int i = frequencies.size(); i > frequencies.size() - 15; i--) {
			frequentNumbers.add(frequencies.get(i).getValue());
		}
		while (!frequentNumbers.isEmpty()) {
			int first = frequentNumbers.get(random.nextInt(frequentNumbers.size()));
			int second = frequentNumbers.get(random.nextInt(frequentNumbers.size()));
			int third = frequentNumbers.get(random.nextInt(frequentNumbers.size()));
			int fourth = frequentNumbers.get(random.nextInt(frequentNumbers.size()));
			int fifth = frequentNumbers.get(random.nextInt(frequentNumbers.size()));
			tickets.add(new Ticket(new int[] { first, second, third, fourth, fifth }));
		}
		return tickets;
	}

	private List<Ticket> generateByLeastFrequentNumbers() {
		List<Ticket> tickets = new ArrayList<>();
		List<Integer> leastFrequentNumbers = new ArrayList<>();
		for (int i = frequencies.size(); i > frequencies.size() - 15; i--) {
			leastFrequentNumbers.add(frequencies.get(i).getValue());
		}
		while (!leastFrequentNumbers.isEmpty()) {
			int first = leastFrequentNumbers.get(random.nextInt(leastFrequentNumbers.size()));
			int second = leastFrequentNumbers.get(random.nextInt(leastFrequentNumbers.size()));
			int third = leastFrequentNumbers.get(random.nextInt(leastFrequentNumbers.size()));
			int fourth = leastFrequentNumbers.get(random.nextInt(leastFrequentNumbers.size()));
			int fifth = leastFrequentNumbers.get(random.nextInt(leastFrequentNumbers.size()));
			tickets.add(new Ticket(new int[] { first, second, third, fourth, fifth }));
		}
		return tickets;
	}
}