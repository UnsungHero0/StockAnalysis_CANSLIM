package com.download.historicaldatadownload.yahoo.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.naming.spi.DirStateFactory.Result;

import com.download.historicaldatadownload.yahoo.jdbc.dao.CodeListsDao;

public class updateSectionInfo {

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

	public updateSectionInfo() {
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
		updateSectionInfo.code = code;
	}

	public static Integer getPage() {
		return page;
	}

	public static void setPage(Integer page) {
		updateSectionInfo.page = page;
	}

	public static Date getLatestDate() {
		return latestDate;
	}

	public static void setLatestDate(Date latestDate) {
		updateSectionInfo.latestDate = latestDate;
	}

	public static Boolean getIfUpdateOver() {
		return ifUpdateOver;
	}

	public static void setIfUpdateOver(Boolean ifUpdateOver) {
		updateSectionInfo.ifUpdateOver = ifUpdateOver;
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
		updateSectionInfo.getQuotesUrl = getQuotesUrl;
	}

	public static void main(String args[]) {
		// TODO
		CodeListsDao clDao = new CodeListsDao();
		ArrayList<String> codeLists = clDao.getCodeLists();
		Connection con = null;
		System.out.println("start updating quotes...");
		int count = 0;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			for (String code : codeLists) {
				// if (Integer.parseInt(code) > 9992) {
				setCode(code);
				update(code, con);
				ifUpdateOver = false;
				System.out.print(code + " is updated, ");
				System.out.println(codeLists.size() - count++ + " to go!");
				// }
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void update(String code, Connection con) {

		try {
			latestDate = findLatestDateInDB(code, con);
			URL url = new URL(getQuotesUrl);
			HttpURLConnection set = (HttpURLConnection) url.openConnection();
			set.setRequestProperty("Accept-Language", "jp");
			set.setReadTimeout(1000 * 30);
			BufferedReader fi = null;
			Boolean ifReaded = false;
			Integer recordNumber = findRecordNumber(code);
			Integer loop = recordNumber / 50 + 1;
			for (page = 1; page < loop && ifUpdateOver == false; page++) {
				while (ifReaded.equals(false)) {
					try {
						set.disconnect();
						String getQuotes = "http://info.finance.yahoo.co.jp/history/?code="
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
								+ month
								+ "&ed="
								+ day
								+ "&tm=d&p=" + getPage();
						url = new URL(getQuotes);
						set = (HttpURLConnection) url.openConnection();
						set.setReadTimeout(1000 * 30);
						set.connect();
						fi = new BufferedReader(new InputStreamReader(
								set.getInputStream()));
						ifReaded = true;
					} catch (SocketTimeoutException e) {
						System.out.println("time out " + loop);
					}
				}
				String input = "";
				while ((input = fi.readLine()) != null) {
					insertDayQuoteToDB(input, code, con);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			Connection con) {
		String string1 = "";
		String date = "";
		if (input.startsWith("</tr>") && ifUpdateOver == false) {
			if (!input.contains("分割")) {
				String inputRow[] = input.split("</tr><tr><td>");
				for (String string : inputRow) {
					// System.out.println(string);
					// System.out.println(string.charAt(0));
					try {
						if (Character.isDigit(string.charAt(0))) {
							if (string.contains("</table>")) {
								string = string.substring(0,
										string.length() - 13);
							}
							if (string.contains("class")) {
								// System.out.println(string);
								string1 = string.substring(0,
										string.indexOf("class") - 14);
								// System.out.println(string1);
								string = string.substring(
										string.indexOf("\">") + 6,
										string.length());
								// System.out.println(string);
								string1 += "</td><td>";
								String inputArray[] = string1
										.split("</td><td>");
								ArrayList<Integer> result = new ArrayList<>();
								for (int i = 0; i < inputArray.length; i++) {
									if (i == 0) {
										String elementString = inputArray[0];
										date = turnToYear(elementString);
									} else {
										String elementString = subComma(inputArray[i]);
										result.add(Integer
												.parseInt(elementString));
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
							}
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
						}
					} catch (StringIndexOutOfBoundsException e) {
					}
				}
			} else {
				createNewQuotesTable(code, con);
			}
		}
	}

	public static int findRecordNumber(String code) {
		int result = 0;
		try {
			URL url = new URL("http://info.finance.yahoo.co.jp/history/?code="
					+ code + ".T&sy=1983&sm=1&sd=1&ey=" + year + "&em=" + month
					+ "&ed=" + day + "&tm=d&p=1");
			HttpURLConnection set = (HttpURLConnection) url.openConnection();
			set.setRequestProperty("Accept-Language", "jp");
			set.connect();
			BufferedReader fi = new BufferedReader(new InputStreamReader(
					set.getInputStream()));
			String input;

			while ((input = fi.readLine()) != null) {
				if (input.contains("stocksHistoryPageing")) {
					int endpoint = input.indexOf("件中");
					int startpoint = input.indexOf("件");
					result = Integer.valueOf(input.substring(startpoint + 2,
							endpoint));
					break;
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public static void createNewQuotesTable(String code, Connection con) {
		deleteQuotesTable(code, con);
		String sqlCreateTable = "CREATE TABLE IF NOT EXISTS `TokyoStockExchange_test`.`"
				+ "?_HistoricalQuotes_Tokyo"
				+ "` ("
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
		
		try {
			URL url = new URL(getQuotesUrl);
			HttpURLConnection set = (HttpURLConnection) url.openConnection();
			set.setRequestProperty("Accept-Language", "jp");
			set.setReadTimeout(1000 * 30);
			BufferedReader fi = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deleteQuotesTable(String code, Connection con) {
		String deleteQutoesTalbeSql = "DROP TABLE ?_HistoricalQuotes_Tokyo";
		try {
			PreparedStatement stmt = con.prepareStatement(deleteQutoesTalbeSql);
			stmt.setInt(1, Integer.valueOf(code));
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
