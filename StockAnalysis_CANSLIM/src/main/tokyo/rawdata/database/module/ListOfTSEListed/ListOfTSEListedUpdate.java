package module.ListOfTSEListed;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import commontool.JDBCUtil;
import dao.UrlDao;
import namespace.DBNameSpace;

public class ListOfTSEListedUpdate {

	private static final String GETDBCODESQL = "SELECT DISTINCT(Local_Code) code FROM "
			+ DBNameSpace.getListedcompaniesTokyoDb();

	public static void updataListedCompanyList(Connection con) {

		// gather latestInfo
		HashMap<String, ArrayList<ArrayList<String>>> listedInfo = getListedInfo();

		// gather all latest local code
		ArrayList<String> latestCodeList = getLatestCode(listedInfo);

		// gather all local code in DB
		ArrayList<String> dbCodeList = getDBCode(con);

		// find the new code
		ArrayList<String> newCodeList = findNewCode(latestCodeList, dbCodeList);
		
		// Insert into Table
		insertIntoDB(listedInfo, newCodeList, con);

	}

	public static HashMap<String, ArrayList<ArrayList<String>>> getListedInfo() {
		HashMap<String, ArrayList<ArrayList<String>>> result = new HashMap<>();
		HashMap<String, String> listedURL = ListOfTSEListedFileURL
				.getAllListURL();
		for (String sectionName : listedURL.keySet()) {
			result.put(sectionName,
					UrlDao.getExcelFromUrl(listedURL.get(sectionName)));
		}
		return result;
	}

	public static ArrayList<String> getLatestCode(
			HashMap<String, ArrayList<ArrayList<String>>> input) {
		ArrayList<String> result = new ArrayList<>();
		for (ArrayList<ArrayList<String>> element : input.values()) {
			for (ArrayList<String> oneRecord : element) {
				result.add(oneRecord.get(1));
			}
		}
		return result;
	}

	public static ArrayList<String> getDBCode(Connection con) {
		ArrayList<String> result = new ArrayList<>();
		ResultSet rs = JDBCUtil.excuteQueryWithResult(GETDBCODESQL, con);
		try {
			while (rs.next()) {
				result.add(rs.getString("code"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<String> findNewCode(
			ArrayList<String> latestCodeList, ArrayList<String> dbCodeList) {
		ArrayList<String> result = new ArrayList<>();
		for (String code : latestCodeList) {
			if (!dbCodeList.contains(code)) {
				result.add(code);
			}
		}
		return result;
	}

	public static void insertIntoDB(
			HashMap<String, ArrayList<ArrayList<String>>> input,
			ArrayList<String> newCode, Connection con) {

		for (String sectionName : input.keySet()) {
			String field = "";
			String value = "";
			for (int i = 0; i < input.get(sectionName).size(); i++) {
				if (newCode.contains(input.get(sectionName).get(i).get(1))) {
					if (i == 0) {
						field = getField(input.get(sectionName).get(i));
					} else {
						value += getValue(input.get(sectionName).get(i),
								sectionName) + ",";

					}
				}
			}
			if (value.contains(",")) {
				value = value.substring(0, value.length() - 1);
				JDBCUtil.insertData(DBNameSpace.getListedcompaniesTokyoDb(),
						field, value, con);
			}
		}

	}

	public static String getField(ArrayList<String> input) {
		String field = "(";
		for (String columnName : input) {
			field += columnNameCovertion(columnName) + ",";
		}
		field = field + "Department,Country)";
		return field;
	}

	public static String getValue(ArrayList<String> input, String sectionName) {
		String value = "(";
		for (String oneValue : input) {
			if (oneValue.equals("-") && oneValue.length() == 1) {
				value += "null,";
			} else if (!ifAllnumber(oneValue)) {
				value += "'" + modifyName(oneValue) + "',";
			} else {
				value += oneValue + ",";
			}
		}
		value = value + "'" + sectionName + "','Tokyo')";
		return value;
	}

	public static Boolean ifAllnumber(String input) {
		Boolean result = true;
		for (char element : input.toCharArray()) {
			if (!Character.isDigit(element)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public static String columnNameCovertion(String str) {
		if (str.equals("Effective Date")) {
			return "Effective_Date";
		} else if (str.equals("Local Code")) {
			return "Local_Code";
		} else if (str.equals("Name (English)")) {
			return "Name_English";
		} else if (str.equals("33 Sector(Code)")) {
			return "33_Sector_code";
		} else if (str.equals("33 Sector(name)")) {
			return "33_Sector_name";
		} else if (str.equals("17 Sector(Code)")) {
			return "17_Sector_code";
		} else if (str.equals("17 Sector(name)")) {
			return "17_Sector_name";
		} else if (str.equals("Size Code (New Index Series)")) {
			return "Size_Code_New_Index_Series";
		} else if (str.equals("Size (New Index Series)")) {
			return "Size_New_Index_Series";
		}
		return null;
	}

	public static String modifyName(String str) {
		String result = "";
		for (char element : str.toCharArray()) {
			if (String.valueOf(element).equals("'")) {
				result += "\\'";
			} else {
				result += String.valueOf(element);
			}
		}
		return result;
	}

}
