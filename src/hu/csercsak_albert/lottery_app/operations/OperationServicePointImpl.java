package hu.csercsak_albert.lottery_app.operations;

import java.util.ArrayList;
import java.util.List;

import hu.csercsak_albert.lottery_app.general.DefinitonException;
import hu.csercsak_albert.lottery_app.main.Operation;
import hu.csercsak_albert.lottery_app.main.OperationServicePoint;

public class OperationServicePointImpl implements OperationServicePoint {

	private static final OperationServicePoint INSTANCE = new OperationServicePointImpl();

	public static OperationServicePoint getInstance() {
		return INSTANCE;
	}

	private OperationServicePointImpl() {
	}

	private final List<Operation> operations = List.of(new ReadDraws(), new PrintBiggestPrizes(), //
			new AnalysisPrinter(), new Settings(), new ExactDateAnalyze());

	@Override
	public List<Operation> getAvailableOperations() {
		return operations;
	}

	@Override
	public Operation getOps(String shortName) throws DefinitonException {
		for (var op : operations) {
			if (op.getShortName().equals(shortName)) {
				return op;
			}
		}
		throw new DefinitonException();
	}

	@Override
	public List<Operation> getOps(String[] shortNames) throws DefinitonException {
		List<Operation> ops = new ArrayList<>();
		for (var name : shortNames) {
			ops.add(getOps(name));
		}
		return ops;
	}

}
