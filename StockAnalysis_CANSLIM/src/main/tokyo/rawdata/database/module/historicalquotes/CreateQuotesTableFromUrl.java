package module.historicalquotes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import dao.UrlDao;

public class CreateQuotesTableFromUrl {

	private final static String year = String.valueOf(Calendar.getInstance()
			.get(Calendar.YEAR));
	private final static String month = String.valueOf(Calendar.getInstance()
			.get(Calendar.MONTH) + 1);
	private final static String day = String.valueOf(Calendar.getInstance()
			.get(Calendar.DATE));
	private final static String startYear = "1983";
	private final static String startMonth = "1";
	private final static String startDay = "1";
	private static String code = "";
	private static Integer page = 1;
	private final static String deleteQutoesTalbeSql = "DROP TABLE IF EXISTS ?_HistoricalQuotes_Tokyo";
	private final static String initialNewQuotesTableSql = "CREATE TABLE IF NOT EXISTS TokyoStockExchange_test."
			+ "?_HistoricalQuotes_Tokyo"
			+ " ("
			+ "`Country` VARCHAR(50) NOT NULL Default ?,"
			+ "`Local_Code` INT NOT NULL Default ?,"
			+ "`Name_English` VARCHAR(100) NOT NULL Default ?,"
			+ "`Date` DATE NOT NULL,"
			+ "`Open` INT NOT NULL,"
			+ "`High` INT NOT NULL,"
			+ "`Low` INT NOT NULL,"
			+ "`Close` INT NOT NULL,"
			+ "`Volume` INT NOT NULL,"
			+ "`AdjClose` INT NOT NULL,"
			+ "PRIMARY KEY (`Date`))"
			+ "ENGINE = InnoDB;";

	public static String getCode() {
		return code;
	}

	public static void setCode(String code) {
		CreateQuotesTableFromUrl.code = code;
	}

	public static Integer getPage() {
		return page;
	}

	public static void setPage(Integer page) {
		CreateQuotesTableFromUrl.page = page;
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

	public static String getInitialnewquotestablesql() {
		return initialNewQuotesTableSql;
	}

	public static String getDeletequtoestalbesql() {
		return deleteQutoesTalbeSql;
	}

	public CreateQuotesTableFromUrl() {
		// TODO Auto-generated constructor stub
	}

	public static void createNewQuotesTable(String code, Connection con) {
		setCode(code);
		deleteQuotesTable(code, con);
		initialNewQuotesTable(code, con);
		Integer recordNumber = findRecordNumber(code);
		Integer loop = recordNumber / 50 + 1;
		System.out.print("stock " + code + " start to update.. " + loop + ": ");
		for (page = 1; page <= loop; page++) {
			String getQuotesUrl = "http://info.finance.yahoo.co.jp/history/?code="
					+ getCode()
					+ ".T&sy="
					+ startYear
					+ "&sm="
					+ startMonth
					+ "&sd="
					+ startDay
					+ "&ey="
					+ year
					+ "&em="
					+ month
					+ "&ed=" + day + "&tm=d&p=" + page;
			ArrayList<String> inputList = UrlDao.getUrlBuffer(getQuotesUrl);
			for (String input : inputList) {
				insertDayQuoteToDB(input, code, con);
			}
			System.out.print(page + " ");
		}
		System.out.println("\n" + "stock " + code + " is finished");
	}

	public static void deleteQuotesTable(String code, Connection con) {
		try {
			PreparedStatement stmt = con.prepareStatement(deleteQutoesTalbeSql);
			stmt.setInt(1, Integer.valueOf(code));
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void initialNewQuotesTable(String code, Connection con) {

		try {
			String sqlFindName = "SELECT Name_English FROM Section_Tokyo WHERE Local_Code = "
					+ code;
			ResultSet rs = con.prepareStatement(sqlFindName).executeQuery();
			rs.next();
			String name = rs.getString("Name_English");
			PreparedStatement stmt = con
					.prepareStatement(initialNewQuotesTableSql);
			stmt.setInt(1, Integer.valueOf(code));
			stmt.setString(2, "Japan");
			stmt.setInt(3, Integer.valueOf(code));
			stmt.setString(4, name);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public static void insertDayQuoteToDB(String input, String code,
			Connection con) {
		String string1 = "";
		String date = "";
		if (input.startsWith("</tr>")) {
			String inputRow[] = input.split("</tr><tr><td>");
			for (String string : inputRow) {
				// System.out.println(string);
				// System.out.println(string.charAt(0));
				try {
					if (Character.isDigit(string.charAt(0))) {
						if (string.contains("</table>")) {
							string = string.substring(0, string.length() - 13);
						}
						if (string.contains("class") && !string.contains("分割")) {
							string1 = string.substring(0,
									string.indexOf("class") - 14);
							// System.out.println(string1);
							string = string.substring(
									string.indexOf("\">") + 6, string.length());
							// System.out.println(string);
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
							insertIntoDB(date, result, code, con);
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
							insertIntoDB(date, result, code, con);
						}
					}
				} catch (StringIndexOutOfBoundsException e) {
				}
			}

		}
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
			String code, Connection con) {
		String insertOneDayQuoteSql = "INSERT INTO ?_HistoricalQuotes_Tokyo"
				+ "(Date, Open, High, Low, Close, Volume, AdjClose) VALUES "
				+ "(?,?,?,?,?,?,?)";
		try {
			PreparedStatement stmt = con.prepareStatement(insertOneDayQuoteSql);
			stmt.setInt(1, Integer.parseInt(code));
			stmt.setString(2, date);
			for (int i = 0; i < quotes.size(); i++) {
				stmt.setInt(i + 3, quotes.get(i));
			}
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
