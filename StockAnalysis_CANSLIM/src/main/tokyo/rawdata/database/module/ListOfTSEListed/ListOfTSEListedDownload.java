package module.ListOfTSEListed;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import commontool.JDBCUtil;
import dao.UrlDao;
import namespace.DBNameSpace;

public class ListOfTSEListedDownload {
	private final static String DROPTABLESQL = "DROP TABLE IF EXISTS "
			+ DBNameSpace.getListedcompaniesTokyoDb();

	private final static String CREATETABLESQL = "CREATE TABLE IF NOT EXISTS "
			+ DBNameSpace.getListedcompaniesTokyoDb() + " ("
			+ "`Effective_Date` DATE NOT NULL,"
			+ "`Country` VARCHAR(50) NOT NULL,"
			+ "`Department` VARCHAR(100) NOT NULL,"
			+ "`Local_Code` INT NOT NULL,"
			+ "`Name_English` VARCHAR(100) NOT NULL," + "`33_Sector_code` INT,"
			+ "`33_Sector_name` VARCHAR(100)," + "`17_Sector_code` INT,"
			+ "`17_Sector_name` VARCHAR(100),"
			+ "`Size_Code_New_Index_Series` INT,"
			+ "`Size_New_Index_Series` VARCHAR(100),"
			+ "PRIMARY KEY (`Local_Code`, `Name_English`))";

	public static void downloadListedCompanyList(Connection con) {
			// drop table
			dropTable(con);

			// create table
			createTable(con);

			// gather latestInfo
			HashMap<String, ArrayList<ArrayList<String>>> listedInfo = getListedInfo();

			// Insert into Table
			insertIntoDB(listedInfo, con);

	}

	public static void dropTable(Connection con) {
		JDBCUtil.excuteQuery(DROPTABLESQL, con);
	}

	public static void createTable(Connection con) {
		JDBCUtil.excuteQuery(CREATETABLESQL, con);
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

	public static void insertIntoDB(
			HashMap<String, ArrayList<ArrayList<String>>> input, Connection con) {
		for (String sectionName : input.keySet()) {
			String field = "";
			String value = "";
			for (int i = 0; i < input.get(sectionName).size(); i++) {
				if (i == 0) {
					field = getField(input.get(sectionName).get(i));
				} else {
					value += getValue(input.get(sectionName).get(i),sectionName) + ",";
					
				}
			}
			value = value.substring(0,value.length()-1);
			JDBCUtil.insertData(
					DBNameSpace.getListedcompaniesTokyoDb(), field,
					value, con);
		}
		// TODO
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
			} else if (!ifAllnumber(oneValue) ) {
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
		for(char element : input.toCharArray()) {
			if(!Character.isDigit(element)) {
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
