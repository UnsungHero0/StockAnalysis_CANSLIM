package commontool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;

public class JDBCUtil {

	public JDBCUtil() {
		// TODO Auto-generated constructor stub
	}

	public static Boolean hasTable(String tableName,Connection con){
		String sql = "DESC "+ tableName + "";
		Boolean result = true;
		try {
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			result = false;
			if (!e.getMessage().contains("exist")) {
				e.printStackTrace();
				
			}
		}
		return result;
	}
	
	public static void dropTable(String tableName, Connection con) {
		String sql ="DROP TABLE IF EXISTS "+ tableName + "";
		try {
			con.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void excuteQuery(String query, Connection con) {
		try {
			con.prepareStatement(query).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet excuteQueryWithResult(String query, Connection con) {
		ResultSet rs = null;
		try {
			rs = con.prepareStatement(query).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static ResultSet excuteQueryWithResult(PreparedStatement stmt) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void insertData(String tableName, String field, String value, Connection con) {
		String insertSql = "INSERT INTO "
				+ tableName
				+ " " + field + " VALUES " + value;
		try {
			con.prepareStatement(insertSql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
