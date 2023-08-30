package hu.csercsak_albert.lottery_app.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {

	private Constants() {
	}

	private static final String ERROR_MESSAGE = "ERROR! %s , the application can not start!%n";

	private static final Properties SQL_PROPERTIES = new Properties();
	private static final String SQL_PROP_PATH = "src/hu/csercsak_albert/lottery_app/resources/sql.properties";
	static {
		try (var fos = new FileInputStream(SQL_PROP_PATH)) {
			SQL_PROPERTIES.load(fos);
		} catch (IOException e) {
			System.err.printf(ERROR_MESSAGE, e.getMessage());
			throw new RuntimeException();
		}
	}

	private static final Properties MENU_PROPERTIES = new Properties();
	private static final String MENU_PROP_PATH = "src/hu/csercsak_albert/lottery_app/resources/menu.properties";
	static {
		try (var fos = new FileInputStream(MENU_PROP_PATH)) {
			MENU_PROPERTIES.load(fos);
		} catch (IOException e) {
			System.err.printf(ERROR_MESSAGE, e.getMessage());
			throw new RuntimeException();
		}
	}

	private static final Properties HTML_PROPERTIES = new Properties();

	static {
		try (var fos = new FileInputStream("src/hu/csercsak_albert/lottery_app/resources/html.properties")) {
			HTML_PROPERTIES.load(fos);
		} catch (IOException e) {
			System.err.printf(ERROR_MESSAGE, e.getMessage());
			throw new RuntimeException();
		}
	}

	public static Properties getSqlProperties() {
		return SQL_PROPERTIES;
	}

	public static String getSqlPropertiesPath() {
		return SQL_PROP_PATH;
	}

	public static Properties getMenuProperties() {
		return MENU_PROPERTIES;
	}

	public static String getMenuPropertiesPath() {
		return MENU_PROP_PATH;
	}

	public static Properties getHtmlProperties() {
		return HTML_PROPERTIES;
	}

}
