package hu.csercsak_albert.lottery_app.main;

import java.util.List;

import hu.csercsak_albert.lottery_app.general.DefinitonException;

public interface OperationServicePoint {

	List<Operation> getAvailableOperations();

	Operation getOps(String shortName) throws DefinitonException;

	// returns the operation objects specified by their short names
	List<Operation> getOps(String[] shortName) throws DefinitonException;

}
