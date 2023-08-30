package hu.csercsak_albert.lottery_app.operations;

import java.time.LocalDate;
import java.util.List;

import hu.csercsak_albert.lottery_app.data_service.Draw;
import hu.csercsak_albert.lottery_app.general.DatabaseException;

public interface DatabaseContactPoint {

	List<Draw> getAllDraw() throws DatabaseException;

	List<Draw> getDraws(LocalDate from, LocalDate to) throws DatabaseException;

	Draw getDraw(LocalDate date) throws DatabaseException;

	int getDrawsCount() throws DatabaseException;

	double calculateEuro(long prize, LocalDate date) throws DatabaseException;

	Draw getFiveHitDrawBefore(Draw draw) throws DatabaseException;

	LocalDate getOldestDrawDate() throws DatabaseException;

	LocalDate getLatestDrawDate() throws DatabaseException;

	List<Draw> getDrawsOfHighestPrizes(int count) throws DatabaseException;
}
