package module.listedcompanydownload;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import tool.charDeal;
import tool.consolePrint;
import namespace.SydneyDBNameSpace;
import commontool.JDBCUtil;
import dao.DBSydenyDao;
import dao.DateDao;
import dao.UrlDao;

public class UpdateListedCompanySydeny {

	public static void updateListedCompanyList(Connection con) {

		// 1.get latest list from the web site
		HashMap<String, ArrayList<String>> latest_listed_company = getLaestListedCompanyFromUrl();

		// 2. get old list from DB
		ArrayList<String> old_code_list = getOldListedCompanyFromDB(con);

		// 3. find new listed company
		HashMap<String, ArrayList<String>> new_code_company = getnewListedCompany(
				latest_listed_company, old_code_list);

		// TODO
		// 4.update listed company table in DB
		insertDataIntoDB(new_code_company, con);

	}

	private static HashMap<String, ArrayList<String>> getLaestListedCompanyFromUrl() {
		String listedCompanyListUrl = "http://www.asx.com.au/asx/research/ASXListedCompanies.csv";
		ArrayList<String> urlResult = UrlDao.getUrlBuffer(listedCompanyListUrl);
		HashMap<String, ArrayList<String>> latest_content = new HashMap<>();
		for (int i = 0; i < urlResult.size(); i++) {
			String[] line = urlResult.get(i).split("\"");
			if (i > 3) {
				ArrayList<String> lineResult = new ArrayList<>();

				for (int j = 1; j < line.length; j++) {
					String element = (i == 1) ? line[j] : charDeal
							.subComma(line[j]);
					lineResult.add(element);
				}
				latest_content.put(lineResult.get(1), lineResult);
			}
		}
		return latest_content;
	}

	private static ArrayList<String> getOldListedCompanyFromDB(Connection con) {
		return DBSydenyDao.getCodeListFromDB(con);
	}

	private static HashMap<String, ArrayList<String>> getnewListedCompany(
			HashMap<String, ArrayList<String>> latest_listed_company,
			ArrayList<String> old_code_list) {
		ArrayList<String> latest_code_list = new ArrayList<>(
				latest_listed_company.keySet());
		for (int i = 0; i < latest_code_list.size(); i++) {
			if (old_code_list.contains(latest_code_list.get(i))) {
				latest_listed_company.remove(latest_code_list.get(i));
			}
		}

		ArrayList<String> new_code_list = new ArrayList<>(
				latest_listed_company.keySet());
		if (new_code_list.size() == 0) {
			consolePrint.println("No new listed company!");
		} else {
			consolePrint.print("New listed company code : ");
			for (int i = 0; i < new_code_list.size(); i++) {
				consolePrint.print(new_code_list.get(i) + " , ");
			}
			consolePrint.println("");
		}

		return latest_listed_company;
	}

	private static void insertDataIntoDB(
			HashMap<String, ArrayList<String>> new_code_company, Connection con) {

		ArrayList<String> key = new ArrayList<>(new_code_company.keySet());

		if (new_code_company.keySet().size() > 0) {
			String addedContentQuery = "";
			for (int i = 0; i < key.size(); i++) {
				String result = "('" + DateDao.dateTodayInMysqlForm()
						+ "','Sydney','";
				for (int j = 2; j >= 0; j--) {
					result += dealChar(new_code_company.get(key.get(i)).get(j))
							+ "','";
				}
				result = result.substring(0, result.length() - 2) + "),";
				addedContentQuery += result;
			}
			addedContentQuery = addedContentQuery.substring(0, addedContentQuery.length() - 1);
			String insertDataIntoDB = "INSERT INTO "
					+ SydneyDBNameSpace.getListedcompaniesSydneyDb()
					+ " (Effective_Date, Country,Department,Local_Code,Name_English) VALUES "
					+ addedContentQuery;
			JDBCUtil.excuteQuery(insertDataIntoDB, con);
		}
		consolePrint.println(new_code_company.keySet()
				+ " has added into table!");

		// 2. update effective data in table
		String updateEffectiveDateQuery = "update "
				+ namespace.SydneyDBNameSpace.getListedcompaniesSydneyDb()
				+ " set Effective_date = '" + DateDao.dateTodayInMysqlForm()
				+ "'";
		JDBCUtil.excuteQuery(updateEffectiveDateQuery, con);
		consolePrint.println("Listed company table is updated to "
				+ DateDao.dateTodayInMysqlForm());
	}

	private static String dealChar(String input) {
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
