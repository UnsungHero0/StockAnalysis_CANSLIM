package com.jdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
