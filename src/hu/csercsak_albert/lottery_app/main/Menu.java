package hu.csercsak_albert.lottery_app.main;

import hu.csercsak_albert.lottery_app.general.FastQuitException;

public interface Menu {

	Operation printAndChoose() throws FastQuitException;
}
