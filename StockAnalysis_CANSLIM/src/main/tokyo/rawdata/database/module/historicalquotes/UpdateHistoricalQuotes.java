package module.historicalquotes;

import impl.update.HistoricalQuoteUpdateMultiThreadVersion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dao.UrlDao;

public class UpdateHistoricalQuotes {
	private final static String year = String.valueOf(Calendar.getInstance()
			.get(Calendar.YEAR));
	private final static String month = String.valueOf(Calendar.getInstance()
			.get(Calendar.MONTH) + 1);
	private final static String day = String.valueOf(Calendar.getInstance()
			.get(Calendar.DATE));
	private final static String startYear = "1983";
	private final static String startMonth = "1";
	private final static String startDay = "1";
	private Boolean ifUpdateOver = false;

	public Boolean getIfUpdateOver() {
		return ifUpdateOver;
	}

	public void setIfUpdateOver(Boolean ifUpdateOver) {
		this.ifUpdateOver = ifUpdateOver;
	}

	public static String getYear() {
		return year;
	}

	public static String getMonth() {
		return month;
	}

	public static String getDay() {
		return day;
	}

	public static String getStartyear() {
		return startYear;
	}

	public static String getStartmonth() {
		return startMonth;
	}

	public static String getStartday() {
		return startDay;
	}

	public void updateCode(String threadName) {
		String code = "";
		Boolean ifHasNext = false;
		synchronized (HistoricalQuoteUpdateMultiThreadVersion.codeLists) {
			if (HistoricalQuoteUpdateMultiThreadVersion.codeLists.size() > 0) {
				code = HistoricalQuoteUpdateMultiThreadVersion.codeLists.get(0);
				HistoricalQuoteUpdateMultiThreadVersion.codeLists.remove(0);
				ifHasNext = true;
			}
		}
		while (ifHasNext == true) {
			update(code);
			synchronized (HistoricalQuoteUpdateMultiThreadVersion.count) {
				System.out.println("HistoricalQuotes : " + threadName + ": " + year + "/" + month + "/"
						+ day + "  :  " + code + " is updated, "
						+ --HistoricalQuoteUpdateMultiThreadVersion.count
						+ " to go!");
			}
			synchronized (HistoricalQuoteUpdateMultiThreadVersion.codeLists) {
				if (HistoricalQuoteUpdateMultiThreadVersion.codeLists.size() > 0) {
					code = HistoricalQuoteUpdateMultiThreadVersion.codeLists
							.get(0);
					HistoricalQuoteUpdateMultiThreadVersion.codeLists.remove(0);
				} else {
					ifHasNext = false;
				}
			}
		}
	}

	public void update(String code) {
		try {
			if (tableIsExist(code)) {
				Date latestDate = findLatestDateInDB(code);
				Integer recordNumber = findRecordNumber(code);
				Integer loop = recordNumber / 50 + 1;
				for (int page = 1; page < loop && ifUpdateOver == false; page++) {
					String getQuotesUrl = "http://info.finance.yahoo.co.jp/history/?code="
							+ code
							+ ".T&sy="
							+ startYear
							+ "&sm="
							+ startMonth
							+ "&sd="
							+ startDay
							+ "&ey="
							+ year
							+ "&em="
							+ month + "&ed=" + day + "&tm=d&p=" + page;
					ArrayList<String> inputList = UrlDao
							.getUrlBuffer(getQuotesUrl);
					for (String input : inputList) {
						insertDayQuoteToDB(input, code, latestDate);
					}
				}
			} else {
				synchronized (HistoricalQuoteUpdateMultiThreadVersion.con) {
					CreateQuotesTableFromUrl create = new CreateQuotesTableFromUrl();
					create.createNewQuotesTable(code,
							HistoricalQuoteUpdateMultiThreadVersion.con);
				}
			}
		} catch (StockSplitException e) {
			e.printStackTrace();
			synchronized (HistoricalQuoteUpdateMultiThreadVersion.con) {
				CreateQuotesTableFromUrl create = new CreateQuotesTableFromUrl();
				create.createNewQuotesTable(code,
						HistoricalQuoteUpdateMultiThreadVersion.con);
			}
		}
	}

	public static Boolean tableIsExist(String code) {
		Boolean result = false;
		try {
			String findTableSql = "SHOW TABLES LIKE '" + code
					+ "_HistoricalQuotes_Tokyo'";
			ResultSet rs = null;
			synchronized (HistoricalQuoteUpdateMultiThreadVersion.con) {
				rs = HistoricalQuoteUpdateMultiThreadVersion.con
						.prepareStatement(findTableSql).executeQuery();
			}
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date findLatestDateInDB(String code) {
		Date date = new Date();
		try {
			String selectLatestDaySql = "SELECT MAX(Date) Date FROM "
					+ "?_HistoricalQuotes_Tokyo";
			PreparedStatement stmt = null;
			ResultSet rs = null;
			synchronized (HistoricalQuoteUpdateMultiThreadVersion.con) {
				stmt = HistoricalQuoteUpdateMultiThreadVersion.con
						.prepareStatement(selectLatestDaySql);
				stmt.setInt(1, Integer.parseInt(code));
				rs = stmt.executeQuery();
			}
			rs.next();
			date = rs.getDate("Date");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return date;
	}

	public void insertDayQuoteToDB(String input, String code, Date latestDate)
			throws StockSplitException {
		String string1 = "";
		String date = "";
		if (input.startsWith("</tr>") && ifUpdateOver == false) {
			String inputRow[] = input.split("</tr><tr><td>");
			for (String string : inputRow) {
				try {
					if (Character.isDigit(string.charAt(0))) {
						if (string.contains("</table>")) {
							string = string.substring(0, string.length() - 13);
						}
						if (string.contains("class") && !string.contains("分割")) {
							string1 = string.substring(0,
									string.indexOf("class") - 14);
							string = string.substring(
									string.indexOf("\">") + 6, string.length());
							string1 += "</td><td>";
							String inputArray[] = string1.split("</td><td>");
							ArrayList<Integer> result = new ArrayList<>();
							for (int i = 0; i < inputArray.length; i++) {
								if (i == 0) {
									String elementString = inputArray[0];
									date = turnToYear(elementString);
								} else {
									String elementString = subComma(inputArray[i]);
									result.add(Integer.parseInt(elementString));
								}
							}
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy/MM/dd");
								Date dateNow = sdf.parse(date);
								if (!dateNow.after(latestDate)) {
									ifUpdateOver = true;
									break;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							insertIntoDB(date, result, code);
						} else if (!string.contains("分割")) {
							string = string.substring(0, string.length() - 5);
							string += "</td><td>";
							String inputArray[] = string.split("</td><td>");
							ArrayList<Integer> result = new ArrayList<>();
							for (int i = 0; i < inputArray.length; i++) {
								if (i == 0) {
									String elementString = inputArray[0];
									date = turnToYear(elementString);
								} else {
									String elementString = subComma(inputArray[i]);
									result.add(Integer.parseInt(elementString));
								}
							}
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy/MM/dd");
								Date dateNow = sdf.parse(date);
								if (!dateNow.after(latestDate)) {
									ifUpdateOver = true;
									break;
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							insertIntoDB(date, result, code);
						} else {
							System.out.println(string);
							throw new StockSplitException("stock " + code
									+ " is splited, need to be updated");
						}
					}
				} catch (StringIndexOutOfBoundsException e) {
				}
			}
		}
	}

	public static int findRecordNumber(String code) {
		int result = 0;
		String urlString = "http://info.finance.yahoo.co.jp/history/?code="
				+ code + ".T&sy=1983&sm=1&sd=1&ey=" + year + "&em=" + month
				+ "&ed=" + day + "&tm=d&p=1";
		ArrayList<String> inputList = UrlDao.getUrlBuffer(urlString);
		for (String input : inputList) {
			if (input.contains("stocksHistoryPageing")) {
				int endpoint = input.indexOf("件中");
				int startpoint = input.indexOf("件");
				result = Integer.valueOf(input.substring(startpoint + 2,
						endpoint));
				break;
			}
		}
		return result;
	}

	public static String turnToYear(String input) {
		String output = "";
		char elementArray[] = input.toCharArray();
		input = "";
		for (char element : elementArray) {
			if (Character.isDigit(element))
				input += String.valueOf(element);
			else
				input += "/";
			output = input.substring(0, input.length() - 1);
		}
		return output;
	}

	public static String subComma(String input) {
		if (input.contains(",")) {
			String inputArray[] = input.split(",");
			input = "";
			for (String elementString : inputArray) {
				if (elementString.contains("<")) {
					elementString = elementString.substring(0,
							elementString.length() - 1);
				}
				input += elementString;
			}
		}
		return input;
	}

	public static void insertIntoDB(String date, ArrayList<Integer> quotes,
			String code) {
		String insertOneDayQuoteSql = "INSERT INTO ?_HistoricalQuotes_Tokyo"
				+ "(Date, Open, High, Low, Close, Volume, AdjClose) VALUES "
				+ "(?,?,?,?,?,?,?)";
		try {
			PreparedStatement stmt = null;
			synchronized (HistoricalQuoteUpdateMultiThreadVersion.con) {
				stmt = HistoricalQuoteUpdateMultiThreadVersion.con
						.prepareStatement(insertOneDayQuoteSql);
				stmt.setInt(1, Integer.parseInt(code));
				stmt.setString(2, date);
				for (int i = 0; i < quotes.size(); i++) {
					stmt.setInt(i + 3, quotes.get(i));
				}
				stmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
