package impl.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import module.historicalquotes.CreateQuotesTableFromUrl;
import module.historicalquotes.StockSplitException;
import dao.UrlDao;
import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;

public class HistoricalQuoteUpdate {

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
	private static Date latestDate = new Date();
	private static Boolean ifUpdateOver = false;

	public HistoricalQuoteUpdate() {
		// TODO Auto-generated constructor stub
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

	public static String getSqlquotesurl() {
		return getQuotesUrl;
	}

	public static String getCode() {
		return code;
	}

	public static void setCode(String code) {
		HistoricalQuoteUpdate.code = code;
	}

	public static Integer getPage() {
		return page;
	}

	public static void setPage(Integer page) {
		HistoricalQuoteUpdate.page = page;
	}

	public static Date getLatestDate() {
		return latestDate;
	}

	public static void setLatestDate(Date latestDate) {
		HistoricalQuoteUpdate.latestDate = latestDate;
	}

	public static Boolean getIfUpdateOver() {
		return ifUpdateOver;
	}

	public static void setIfUpdateOver(Boolean ifUpdateOver) {
		HistoricalQuoteUpdate.ifUpdateOver = ifUpdateOver;
	}

	private static String getQuotesUrl = "http://info.finance.yahoo.co.jp/history/?code="
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
			+ "&ed="
			+ day
			+ "&tm=d&p=" + getPage();

	public static String getGetQuotesUrl() {
		return getQuotesUrl;
	}

	public static void setGetQuotesUrl(String getQuotesUrl) {
		HistoricalQuoteUpdate.getQuotesUrl = getQuotesUrl;
	}
	
	public static void main(String args[]) {
		update();
	}

	public static void update() {
		CodeListsDao clDao = new CodeListsDao();
		ArrayList<String> codeLists = clDao.getCodeLists();
		Connection con = null;
		System.out.println("start updating quotes...");
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			for (String code : codeLists) {
				if(Integer.valueOf(code) > 0) {
					setCode(code);
					update(code, con);
					ifUpdateOver = false;
					System.out.print(year + "/" + month + "/" + day + "  :  " + code + " is updated, ");
					System.out.println(codeLists.size()
							- codeLists.indexOf(code) + " to go!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("finished");
	}

	public static void update(String code, Connection con) {

		try {
			if (tableIsExist(code, con)) {
				latestDate = findLatestDateInDB(code, con);
				Integer recordNumber = findRecordNumber(code);
				Integer loop = recordNumber / 50 + 1;
				for (page = 1; page < loop && ifUpdateOver == false; page++) {
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
							+ "&ed="
							+ day
							+ "&tm=d&p="
							+ getPage();
					ArrayList<String> inputList = UrlDao
							.getUrlBuffer(getQuotesUrl);
					for (String input : inputList) {
						insertDayQuoteToDB(input, code, con);
					}
				}
			} else {
				CreateQuotesTableFromUrl.createNewQuotesTable(code, con);
			}
		} catch (StockSplitException e) {
			e.printStackTrace();
			//CreateQuotesTableFromUrl.createNewQuotesTable(code, con);
		}
	}

	public static Boolean tableIsExist(String code, Connection con) {
		Boolean result = false;
		try {
			String findTableSql = "SHOW TABLES LIKE '" + code
					+ "_HistoricalQuotes_Tokyo'";
			ResultSet rs = con.prepareStatement(findTableSql).executeQuery();
			if (rs.next()) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date findLatestDateInDB(String code, Connection con) {
		Date date = new Date();
		try {
			String selectLatestDaySql = "SELECT MAX(Date) Date FROM "
					+ "?_HistoricalQuotes_Tokyo";

			PreparedStatement stmt = con.prepareStatement(selectLatestDaySql);
			stmt.setInt(1, Integer.parseInt(code));
			ResultSet rs = stmt.executeQuery();
			rs.next();
			date = rs.getDate("Date");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static void insertDayQuoteToDB(String input, String code,
			Connection con) throws StockSplitException {
		String string1 = "";
		String date = "";
		if (input.startsWith("</tr>") && ifUpdateOver == false) {
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
							// System.out.println(string);
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
							insertIntoDB(date, result, code, con);
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
