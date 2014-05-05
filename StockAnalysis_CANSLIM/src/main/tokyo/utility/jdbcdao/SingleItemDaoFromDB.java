package jdbcdao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.spi.DirStateFactory.Result;

public class SingleItemDaoFromDB {

	public SingleItemDaoFromDB() {
		// TODO Auto-generated constructor stub
	}
	
	public static HashMap<String, String> fetchDataFromFinancialStatement(ArrayList<String> codeList, String item, Connection con) {
		String codeListString = "(";
		HashMap<String,String> result = new HashMap<>();
		for (String code : codeList) {
			codeListString += code + ", "; 
		}
		codeListString = codeListString.substring(0,codeListString.length()-2) + ")";
		String getDataFromFinancialStatementSql = "SELECT Local_Code, " + item + " FROM FinancialStatementTokyo_test WHERE Local_Code IN " + codeListString;
		try {
			ResultSet rs = con.prepareStatement(getDataFromFinancialStatementSql).executeQuery();
			while (rs.next() == true) {
				result.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
		//TODO
	}
	
	public static ResultSet selectFromDBOrder(String field, String DBName, String orderByField, Connection con) {
		String sql = "SELECT " + field + " FROM " + DBName + " ORDER BY " + orderByField;
		ResultSet rs = null;
		try {
			rs = con.prepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static ResultSet selectFrom(String field, String DBName, Connection con) {
		String sql = "SELECT " + field + " FROM " + DBName;
		ResultSet rs = null;
		try {
			rs = con.prepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
}
