package hu.csercsak_albert.lottery_app.data_service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.csercsak_albert.lottery_app.general.HtmlReadingException;
import hu.csercsak_albert.lottery_app.main.Constants;

class HTMLReader {

	private static final Properties PROPERTIES = Constants.getHtmlProperties();

	private static final Pattern PATTERN = Pattern.compile(PROPERTIES.getProperty("datareader_regex"));
	private static final String URL = PROPERTIES.getProperty("otoslotto_url");

	/**********************************************************************************************
	 * Reading draws
	 */

	List<Draw> readDrawsBackUntil(LocalDate toDateBack) throws IOException {
		List<Draw> list = new ArrayList<>();
		try (var scanner = getHTMLScanner()) {
			Matcher m;
			while (scanner.hasNext()) {
				String input = scanner.nextLine().replaceAll("\\s+", "").replaceAll("<td>|</td>|<tr>|</tr>", " ").replaceAll("\\s+", " ").trim();
				m = PATTERN.matcher(input);
				if (m.matches()) { // Data comes DESC ordered.
					String dateString = m.group("date");
					var formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
					var localDate = LocalDate.parse(dateString, formatter);
					if (localDate.isAfter(toDateBack) || localDate.equals(toDateBack)) {
						list.add(new Draw(m, localDate));
					}
				}
			}
			return list;
		}
	}

	private Scanner getHTMLScanner() throws IOException {
		var url = new URL(URL);
		var inputStream = new InputStreamReader(url.openStream());
		return new Scanner(inputStream);
	}

	/**********************************************************************************************
	 * Reading EUR-HUF exchanges
	 */
	double getValueFromURL(String urlLink) throws HtmlReadingException {
		try (var ins = new URL(urlLink).openStream()) {
			try (var inputStream = new InputStreamReader(ins)) {
				try (var scanner = new Scanner(inputStream)) {
					Pattern pattern = Pattern.compile(".+Close: 1 EUR = (?<value>(\\d{3}\\.\\d{2})) HUF.+");
					Matcher m;
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						m = pattern.matcher(line);
						if (m.matches()) {
							return Double.parseDouble(m.group("value"));
						}
					}
				}
			}
		} catch (IOException e) {
		}
		throw new HtmlReadingException(); // INFO Ends up there too when the EUR value is 0 (Before ~2010)
	}
}
