package hu.csercsak_albert.lottery_app.operations;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import hu.csercsak_albert.lottery_app.general.DatabaseException;
import hu.csercsak_albert.lottery_app.general.FastQuitException;
import hu.csercsak_albert.lottery_app.main.UserInput;

class Settings extends AbstractOperation {

	Settings() {
		super("Settings", "ST");
	}

	private static final int MAX_ORDINAL = ModifiableSettings.values().length;

	@Override
	public void perform(UserInput userInput) throws DatabaseException, FastQuitException {
		System.out.printf("%nWhat do you want to change ?%n");
		int ordinal = 0;
		do {
			printOptions();
			System.out.print(prompt);
			String input = userInput.getNext();
			if (input.equals(fastQuitChar)) {
				throw new FastQuitException();
			}
			try {
				ordinal = Integer.parseInt(input);
			} catch (NumberFormatException e) {
			}
		} while (ordinal < 1 || ordinal > MAX_ORDINAL);
		update(userInput, ModifiableSettings.values()[ordinal - 1]);
		System.out.printf("%n To apply changes, please restart the program!%n");
		// Later: (I18N)
	}

	private void printOptions() {
		for (var option : ModifiableSettings.values()) {
			System.out.printf("%d - %s %s%n", option.ordinal() + 1, option.getName(), option.getSuggestions());
		}
	}

	private void update(UserInput userInput, ModifiableSettings option) throws FastQuitException {
		System.out.printf("%n Enter a new %s :%n", option.getName().toLowerCase());
		String newValue = "";
		do {
			newValue = userInput.getNext();
		} while (!option.check(newValue));
		write(option, newValue);
	}

	private void write(ModifiableSettings option, String newValue) {
		Properties properties = option.getProperties();
		properties.setProperty(option.getPropertiesName(), newValue);
		try (FileOutputStream fos = new FileOutputStream(option.getPropertiesPath())) {
			properties.store(fos, "Updated %s".formatted(option.getPropertiesName()));
		} catch (IOException e) {
			System.err.println("ERROR! " + e.getMessage());
		}
	}
}
