package module.listedcompanydownload;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import tool.consolePrint;
import commontool.JDBCUtil;
import dao.DBSydenyDao;
import dao.DateDao;
import dao.UrlDao;

public class UpdateHistoricalQuotesSydney {

	public static void updateHistoricalQuotesSydney(Connection con) {

		// 1. collect all local code from listed company DB
		ArrayList<String> codeList = new ArrayList<>();
		codeList = DBSydenyDao.getCodeListFromDB(con);
//		codeLizst.add("AEB");
		System.out.println("get Code finished");

		// 2. loop for each quotes update

		for (int i = 0; i < codeList.size(); i++) {
			updateOneCompanyQuotes(codeList.get(i), con);
			consolePrint.println(i + " " + codeList.get(i) + " is update to "
					+ DateDao.dateTodayInMysqlForm() + ", "
					+ (codeList.size() - i) + " is left");
		}

	}

	public static void updateOneCompanyQuotes(String local_code, Connection con) {

		// 1. get exist date in DB
		ArrayList<String> exist_date = getExistDate(local_code, con);

		// 2. get exist date from web
		HashMap<String, ArrayList<String>> latest_quotes = getLatestQuotes(local_code);
		
		
		// 3. find new date
		HashMap<String, ArrayList<String>> new_quotes = getNewQuotes(
				exist_date, latest_quotes);

		// 4. add new date quotes into DB
		if (new_quotes.keySet().size() > 0) {
			ArrayList<ArrayList<String>> content = new ArrayList<>(
					new_quotes.values());
			insertDataToTable(local_code, content, con);
		}
	}

	private static ArrayList<String> getExistDate(String code, Connection con) {
		ArrayList<String> exist_date = new ArrayList<>();
		String getCodeListSql = "SELECT Date FROM "
				+ namespace.SydneyDBNameSpace.getSchemaDb() + code
				+ namespace.SydneyDBNameSpace.getStockhistoricalpriceDb();
		ResultSet rs = null;
		try {
			rs = JDBCUtil.excuteQueryWithResult(getCodeListSql, con);
			while (rs.next()) {
				exist_date.add(rs.getString("Date"));
				// consolePrint.println(rs.getString("Date"));
			}
			rs.close();
		} catch (java.lang.NullPointerException e) {
			consolePrint.println("hello");
			DownloadHistoricalQuotesSydney.createOneHistoricalQuotesTable(code,
					con);
			consolePrint.println(code + " is new created quotes table");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return exist_date;
	}

	public static HashMap<String, ArrayList<String>> getLatestQuotes(String code) {
		HashMap<String, ArrayList<String>> result = new HashMap<>();
		// Date Open High Low Close Volume AdjClose
		String csvUrl = "http://real-chart.finance.yahoo.com/table.csv?s="
				+ code + ".AX&a=00&b=1&c=1980&" + "d="
				+ (Integer.valueOf(DateDao.month) - 1) + "&" + "e="
				+ (DateDao.day) + "&" + "f=" + (DateDao.year) + "&"
				+ "g=d&ignore=.csv";
		ArrayList<String> urlResult = UrlDao.getUrlBuffer(csvUrl);
		if (urlResult.size() > 0) {
			for (int i = 1; i < urlResult.size(); i++) {
				// consolePrint.println(urlResult.get(i).split(",")[0]);
				ArrayList<String> oneQuote = new ArrayList<>();
				for (String element : urlResult.get(i).split(",")) {
					oneQuote.add(element);
				}
				result.put(oneQuote.get(0), oneQuote);
			}
		}
		return result;
	}

	private static HashMap<String, ArrayList<String>> getNewQuotes(
			ArrayList<String> exist_date,
			HashMap<String, ArrayList<String>> latest_quotes) {
		ArrayList<String> latest_date = new ArrayList<>(latest_quotes.keySet());
		for (int i = 0; i < latest_date.size(); i++) {
			if (exist_date.contains(latest_date.get(i))) {
				latest_quotes.remove(latest_date.get(i));
			}
		}
		return latest_quotes;
	}

	public static void insertDataToTable(String code,
			ArrayList<ArrayList<String>> content, Connection con) {
		String tableName = namespace.SydneyDBNameSpace.getSchemaDb() + code
				+ namespace.SydneyDBNameSpace.getStockhistoricalpriceDb();
		// 1. change content to values format
		String values = changeToValues(content);
		// 2. insert into table
		insertDB(tableName, values, con);
	}

	public static String changeToValues(ArrayList<ArrayList<String>> content) {
		String result = "";
		for (ArrayList<String> oneQuote : content) {
			result += "(\'" + oneQuote.get(0) + "\',";
			for (int i = 1; i < oneQuote.size(); i++) {
				result += oneQuote.get(i) + ",";
			}
			result = result.substring(0, result.length() - 1) + "),";
		}
		return result.substring(0, result.length() - 1);
	}

	public static String dealChar(String input) {
		String result = "";
		for (Character ele : input.toCharArray()) {
			if (ele.toString().equals("'")) {
				result += "\\'";
			} else {
				result += ele;
			}
		}
		return result;
	}

	public static void insertDB(String tableName, String values, Connection con) {
		String insertDataSql = "INSERT INTO " + tableName + " "
				+ "(Date, Open, High, Low, Close, Volume, AdjClose) VALUES "
				+ values;
		consolePrint.println(insertDataSql);
		JDBCUtil.excuteQuery(insertDataSql, con);
	}
}
