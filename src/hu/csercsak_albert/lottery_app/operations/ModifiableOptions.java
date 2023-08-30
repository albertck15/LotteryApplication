package hu.csercsak_albert.lottery_app.operations;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.csercsak_albert.lottery_app.general.FastQuitException;
import hu.csercsak_albert.lottery_app.main.Constants;

enum ModifiableSettings {

	TYPE("Type of menu", "type", "1, a, A") {
		@Override
		public boolean check(String input) throws FastQuitException {
			if (input.equals(FAST_QUIT_CHAR_STRING)) {
				throw new FastQuitException();
			}
			if (!"1aA".contains(input)) {
				System.out.println("Should be either 1, a, or A!");
				return false;
			}
			return true;
		}
	},
	PROMPT("Prompt sign", "prompt", null) {
		private static final String REGEX = "[^a-zA-Z0-9]";

		@Override
		public boolean check(String input) throws FastQuitException {
			if (input.equals(FAST_QUIT_CHAR_STRING)) {
				throw new FastQuitException();
			}
			Matcher m = Pattern.compile(REGEX).matcher(input);
			if (m.find()) {
				return true;
			}
			System.out.println("Prompt should contain at least one special character!");
			return false;
		}
	},
	FAST_QUIT_CHAR("Fast quit character or string", "fast_quit_char", "*, X, QUIT") {
		@Override
		public boolean check(String input) throws FastQuitException {
			if (input.isBlank()) {
				System.out.println("Fast quit string cant be blank!");
				return false;
			}
			return true;
		}
	};

	private static final String PROPERTIES_PATH = Constants.getMenuPropertiesPath();
	private static final Properties PROPERTIES = Constants.getMenuProperties();
	private static final String FAST_QUIT_CHAR_STRING = Constants.getMenuProperties().getProperty("fast_quit_char");
	private final String name;
	private final String propertiesName;
	private final String suggestions;

	ModifiableSettings(String name, String propertiesName, String suggestions) {
		this.name = name;
		this.propertiesName = propertiesName;
		this.suggestions = suggestions;
	}

	public Properties getProperties() {
		return PROPERTIES;
	}

	public String getName() {
		return name;
	}

	public String getPropertiesName() {
		return propertiesName;
	}

	public String getSuggestions() {
		return suggestions == null ? "" : "(%s)".formatted(suggestions);
	}

	abstract boolean check(String input) throws FastQuitException;

	public String getPropertiesPath() {
		return PROPERTIES_PATH;
	}
}
