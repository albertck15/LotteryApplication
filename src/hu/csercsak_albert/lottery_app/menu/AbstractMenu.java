package hu.csercsak_albert.lottery_app.menu;

import java.util.List;

import hu.csercsak_albert.lottery_app.general.FastQuitException;
import hu.csercsak_albert.lottery_app.main.Constants;
import hu.csercsak_albert.lottery_app.main.Menu;
import hu.csercsak_albert.lottery_app.main.Operation;
import hu.csercsak_albert.lottery_app.main.UserInput;

abstract class AbstractMenu implements Menu {

	private final String prompt;
	private final List<Operation> operations;
	private final String menuText;
	private final String type;
	private final UserInput userInput;
	private final String fastQuitChar = Constants.getMenuProperties().getProperty("fast_quit_char");

	AbstractMenu(String type, String prompt, List<Operation> operations, UserInput userInput) {
		this.type = type;
		this.prompt = prompt;
		this.operations = operations;
		this.userInput = userInput;
		menuText = createMenuText();

	}

	@Override
	public Operation printAndChoose() throws FastQuitException {
		System.out.printf("%n%s", menuText);
		return askOperation();
	}

	private Operation askOperation() throws FastQuitException {
		for (;;) {
			try {
				System.out.print(prompt);
				String input = userInput.getNext().strip().toLowerCase();
				if (input.equals(fastQuitChar)) {
					throw new FastQuitException();
				}
				int i = getIndexByInput(input);
				return i == operations.size() ? null : operations.get(i);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
	}

	private String createMenuText() {
		var sb = new StringBuilder();
		int counter = 0;
		for (var op : operations) {
			sb.append("%s %s%n".formatted(getNextLabel(type, counter++), op.getName()));
		}
		sb.append("%s %s%n".formatted(getNextLabel(type, counter), "Quit"));
		return sb + "";
	}

	abstract int getIndexByInput(String input);

	abstract String getNextLabel(String type, int i);
}
