package commontool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import namespace.DBNameSpace;

public class JDBCUtil {

	public JDBCUtil() {
		// TODO Auto-generated constructor stub
	}

	public static Boolean hasTable(String tableName,Connection con){
		String sql = "SHOW TABLES LIKE '"+ tableName + "'";
		Boolean result = false;
		try {
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
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
