package module.listedcompanydownload;

import java.sql.Connection;
import java.util.ArrayList;

import commontool.JDBCUtil;
import dao.DBSydenyDao;
import dao.DateDao;
import dao.UrlDao;

public class DownloadHistoricalQuotesSydneyImpl {

	public static void downloadHistoricalQuotesSydneyImpl(Connection con) {

		// 1. collect all local code from listed company DB
		ArrayList<String> codeList = new ArrayList<>();
		codeList = DBSydenyDao.getCodeListFromDB(con);
		//codeList.add("AAC");
		System.out.println("get Code finished");

		// 2. loop for each quotes download
		for (String code : codeList) {
			Character first = Character.valueOf(code.charAt(0));
			if(first >= 'S') {
			createOneHistoricalQuotesTable(code, con);
			System.out.println(code + " is finished, "
					+ (codeList.size() - codeList.indexOf(code)) + " to go");
			}
		}

	}

	public static void createOneHistoricalQuotesTable(String code,
			Connection con) {
		String tableName = namespace.SydneyDBNameSpace.getSchemaDb() + code
				+ namespace.SydneyDBNameSpace.getStockhistoricalpriceDb();

		// 1. get Data, if no data , skip
		ArrayList<ArrayList<String>> quotesList = getQuotesList(code);
		if (quotesList.size() > 0) {
			// 2. drop table (if necessary)
			JDBCUtil.dropTable(tableName, con);

			// 3. create the table
			createTable(code, tableName, con);

			// 4. insert data
			insertDataToTable(code, tableName, quotesList, con);

		}

	}

	public static ArrayList<ArrayList<String>> getQuotesList(String code) {
		// Date Open High Low Close Volume AdjClose
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		String csvUrl = "http://real-chart.finance.yahoo.com/table.csv?s="
				+ code + ".AX&a=00&b=1&c=1980&" + "d="
				+ (Integer.valueOf(DateDao.month) - 1) + "&" + "e="
				+ (DateDao.day) + "&" + "f=" + (DateDao.year) + "&"
				+ "g=d&ignore=.csv";
		ArrayList<String> urlResult = UrlDao.getUrlBuffer(csvUrl);
		if (urlResult.size() > 0) {
			for (int i = 1; i < urlResult.size(); i++) {
				ArrayList<String> oneQuote = new ArrayList<>();
				for (String element : urlResult.get(i).split(",")) {
					oneQuote.add(element);
				}
				result.add(oneQuote);
			}
		}
		return result;
	}

	public static void createTable(String code, String tableName, Connection con) {
		String name = dealChar(DBSydenyDao.getNameEnglish(code, con));
		String createTableSql = "CREATE TABLE IF NOT EXISTS "
				+ tableName + " ("
				+ "`Country` VARCHAR(50) NOT NULL Default 'Sydney',"
				+ "`Local_Code` VARCHAR(10) NOT NULL Default '" + code + "',"
				+ "`Name_English` VARCHAR(100) NOT NULL Default '" + name
				+ "'," + "`Date` DATE NOT NULL," + "`Open` FLOAT NOT NULL,"
				+ "`High` FLOAT NOT NULL," + "`Low` FLOAT NOT NULL,"
				+ "`Close` FLOAT NOT NULL," + "`Volume` DOUBLE NOT NULL,"
				+ "`AdjClose` FLOAT NOT NULL," + "PRIMARY KEY (`Date`))";
		JDBCUtil.excuteQuery(createTableSql, con);
	}

	public static void insertDataToTable(String code, String tableName,
			ArrayList<ArrayList<String>> content, Connection con) {
		// 1. change content to values format
		String values = changeToValues(code, tableName, content, con);
		// 2. insert into table
		insertDB(tableName, values, con);

	}

	public static String changeToValues(String code, String tableName,
			ArrayList<ArrayList<String>> content, Connection con) {
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
				result += "\'";
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
		JDBCUtil.excuteQuery(insertDataSql, con);
	}
}
