package hu.csercsak_albert.lottery_app.menu;

import java.util.List;
import java.util.Properties;

import hu.csercsak_albert.lottery_app.general.DefinitonException;
import hu.csercsak_albert.lottery_app.main.Constants;
import hu.csercsak_albert.lottery_app.main.Menu;
import hu.csercsak_albert.lottery_app.main.Operation;
import hu.csercsak_albert.lottery_app.main.UserInput;

class MenuBuildingPoint {

	private MenuBuildingPoint() {
	}

	private static final Properties PROPERTIES = Constants.getMenuProperties();

	private static final String TYPE = PROPERTIES.getProperty("type");
	private static final String PROMPT = PROPERTIES.getProperty("prompt");

	static Menu getMenu(UserInput userInput, List<Operation> operations) throws DefinitonException {
		return getMenu(TYPE, PROMPT, operations, userInput);
	}

	private static Menu getMenu(String type, String prompt, List<Operation> operations, UserInput userInput) throws DefinitonException {
		if (type.equals("1")) {
			return new NumberedMenu(prompt, operations, userInput);
		}
		try {
			return Character.isLowerCase(type.charAt(0)) ? new LetteredMenu("a", prompt, operations, userInput)
					: new LetteredMenu("A", prompt, operations, userInput);
		} catch (IndexOutOfBoundsException e) {
			throw new DefinitonException();
		}
	}
}