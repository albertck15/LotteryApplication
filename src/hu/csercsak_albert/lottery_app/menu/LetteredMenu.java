package hu.csercsak_albert.lottery_app.menu;

import java.util.List;

import hu.csercsak_albert.lottery_app.main.Operation;
import hu.csercsak_albert.lottery_app.main.UserInput;

class LetteredMenu extends AbstractMenu {

	LetteredMenu(String type, String prompt, List<Operation> operations, UserInput userInput) {
		super(type, prompt, operations, userInput);
	}

	@Override
	String getNextLabel(String type, int i) {
		return (char) (i + (Character.isLowerCase(type.charAt(0)) ? 'a' : 'A')) + ")";
	}

	@Override
	int getIndexByInput(String input) {
		return input.length() == 1 ? input.charAt(0) - 'a' : -1;
	}
}
