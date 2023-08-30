package hu.csercsak_albert.lottery_app.menu;

import java.util.List;

import hu.csercsak_albert.lottery_app.main.Operation;
import hu.csercsak_albert.lottery_app.main.UserInput;

class NumberedMenu extends AbstractMenu {

	NumberedMenu(String prompt, List<Operation> operation, UserInput userInput) {
		super("1", prompt, operation, userInput);
	}

	@Override
	int getIndexByInput(String input) {
		try {
			return Integer.parseInt(input) - 1;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	@Override
	String getNextLabel(String type, int i) {
		return i + 1 + ".";
	}
}
