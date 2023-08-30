package hu.csercsak_albert.lottery_app.menu;

import java.util.List;

import hu.csercsak_albert.lottery_app.general.DefinitonException;
import hu.csercsak_albert.lottery_app.main.Menu;
import hu.csercsak_albert.lottery_app.main.Operation;
import hu.csercsak_albert.lottery_app.main.OperationServicePoint;
import hu.csercsak_albert.lottery_app.main.UserInput;
import hu.csercsak_albert.lottery_app.operations.OperationServicePointImpl;

public class MenuGetter {

	private static final OperationServicePoint SERVICE_POINT = OperationServicePointImpl.getInstance();

	private static final MenuGetter INSTANCE = new MenuGetter();

	private MenuGetter() {
	}

	public static MenuGetter getInstance() {
		return INSTANCE;
	}

	public Menu getMenu(UserInput userInput) throws DefinitonException {
		return getMenu(userInput, SERVICE_POINT.getAvailableOperations());
	}

	public Menu getMenu(UserInput userInput, String... shortNames) throws DefinitonException {
		return getMenu(userInput, SERVICE_POINT.getOps(shortNames));
	}

	public Menu getMenu(UserInput userInput, List<Operation> operations) throws DefinitonException {
		return MenuBuildingPoint.getMenu(userInput, operations);
	}
}
