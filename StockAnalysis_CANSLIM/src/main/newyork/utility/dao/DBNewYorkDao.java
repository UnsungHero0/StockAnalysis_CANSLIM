package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commontool.JDBCUtil;

public class DBNewYorkDao {

	public static ArrayList<String> getCodeListFromDB(Connection con) {
		ArrayList<String> result = new ArrayList<>();
		String getCodeListSql = "SELECT Local_Code FROM "
				+ namespace.NewYorkDBNameSpace.getListedcompaniesNewYorkDb();
		ResultSet rs = null;
		try {
			rs = JDBCUtil.excuteQueryWithResult(getCodeListSql, con);
			while (rs.next()) {
				result.add(rs.getString("Local_Code"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getNameEnglish(String code, Connection con) {
		String result = "";
		String getNameEnglishSql = "SELECT * FROM "
				+ namespace.NewYorkDBNameSpace.getListedcompaniesNewYorkDb()
				+ " WHERE Local_Code = '" + code + "'";
		ResultSet rs = null;
		try {
			rs = JDBCUtil.excuteQueryWithResult(getNameEnglishSql, con);
			rs.next();
			result = rs.getString("Name_English");
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
