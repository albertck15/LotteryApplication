package hu.csercsak_albert.lottery_app.main;

import java.util.Properties;

class TextHolder {

	private static final Properties PROPERTIES = Constants.getSqlProperties();

	void welcome() {
		System.out.printf("Welcome to the Lottery application!%n%n");
	}

	void printInfos() {
		System.out.println("""
				this program uses a self-builded database of the drawn numbers of the hungarian Ötöslottó
				can read specific draws, find the biggest five-hit prizes last years,
				and can generate tickets by most- and least-trending numbers
				 Currently using settings :
				 	Minimum date : %s
				 	Maximum date : %s
				 	Fast quit character/string : %s

				Enjoy!
				""".formatted(PROPERTIES.getProperty("min_date"), PROPERTIES.getProperty("max_date"), //
				Constants.getMenuProperties().getProperty("fast_quit_char")));
	}

	void farewell() {
		System.out.printf("%n%nGoodLuck!%n");
	}
}
