package module.listedcompanydownload;

import java.sql.Connection;
import java.util.ArrayList;

import namespace.SydneyDBNameSpace;
import commontool.JDBCUtil;
import dao.DateDao;
import dao.UrlDao;

public class ListedCompanyDownloadISydneyImpl {

	public ListedCompanyDownloadISydneyImpl() {
		// TODO Auto-generated constructor stub
	}

	private final static String CREATETABLESQL = "CREATE TABLE IF NOT EXISTS "
			+ SydneyDBNameSpace.getListedcompaniesSydneyDb() + " ("
			+ "`Effective_Date` DATE NOT NULL,"
			+ "`Country` VARCHAR(50) NOT NULL,"
			+ "`Department` VARCHAR(100) NOT NULL,"
			+ "`Local_Code` VARCHAR(10) NOT NULL,"
			+ "`Name_English` VARCHAR(100) NOT NULL,"
			+ "PRIMARY KEY (`Local_Code`, `Name_English`))";

	public static void downloadListedCompanyList(Connection con) {

		// 1.get the content from the web site
		ArrayList<ArrayList<String>> content = getListContentFromUrl();

		// 2.drop the table in DB if necessary
		JDBCUtil.dropTable(SydneyDBNameSpace.getListedcompaniesSydneyDb(), con);

		// 3.create the table inDB
		createListedCompanyTalbe(con);

		// 4.insert data into DB
		insertDataIntoDB(content, con);

	}

	public static ArrayList<ArrayList<String>> getListContentFromUrl() {
		ArrayList<ArrayList<String>> finalResult = new ArrayList<>();
		ArrayList<String> subnote = new ArrayList<>();
		subnote.add("0-9");
		for (char item = 'a'; item <= 'z'; item++) {
			subnote.add(String.valueOf(item));
		}
		for (String item : subnote) {
			String urlString = "http://www.asx.com.au/asx/research/listedCompanies.do?coName="
					+ item;
			ArrayList<String> result = UrlDao.getUrlBuffer(urlString);
			for (int i = 0; i < result.size(); i++) {
				if (result.get(i).contains("<td>")
						&& result.get(i).contains("</td>")) {
					// Company Name + Code + GICS industry group
					ArrayList<String> oneResult = new ArrayList<>();
					String output = result.get(i);
					oneResult.add(output.substring(output.indexOf("<td>") + 4,
							output.indexOf("</td>")));
					i++;
					output = result.get(i);
					oneResult.add(output.substring(output.indexOf("('") + 2,
							output.indexOf("('") + 5));
					i++;
					output = result.get(i);
					oneResult.add(output.substring(output.indexOf("<td>") + 4,
							output.indexOf("</td>")));
					finalResult.add(oneResult);
				}
			}
		}
		return finalResult;
	}

	public static void createListedCompanyTalbe(Connection con) {
		JDBCUtil.excuteQuery(CREATETABLESQL, con);
	}

	public static void insertDataIntoDB(ArrayList<ArrayList<String>> content,
			Connection con) {
		String insertDataIntoDB = "INSERT INTO "
				+ SydneyDBNameSpace.getListedcompaniesSydneyDb()
				+ " (Effective_Date, Country,Department,Local_Code,Name_English) VALUES "
				+ changeToValues(content);
		JDBCUtil.excuteQuery(insertDataIntoDB, con);
	}

	public static String changeToValues(ArrayList<ArrayList<String>> content) {
		String finalResult = "";
		for (ArrayList<String> element : content) {
			finalResult += changeToValuesString(element);
		}
		return finalResult.substring(0, finalResult.length() - 1);
	}

	public static String changeToValuesString(ArrayList<String> content) {
		String result = "('" + DateDao.dateTodayInMysqlForm() + "','Sydney','";
		for (int i = 2; i >= 0; i--) {
			result += dealChar(content.get(i)) + "','";
		}
		result = result.substring(0, result.length() - 2) + "),";
		return result;
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

}
