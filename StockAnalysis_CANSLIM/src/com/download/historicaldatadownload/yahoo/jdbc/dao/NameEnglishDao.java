package com.download.historicaldatadownload.yahoo.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class NameEnglishDao {

	private NameEnglishDao() {
		// TODO Auto-generated constructor stub
	}

	public static HashMap<String, String> getNameEnglishDao(Connection con) {
		HashMap<String, String> Name_English = new HashMap<>();
		String selectAllSectionInfoSql = "Select Local_Code, Name_English FROM Section_Tokyo";
		try {
			PreparedStatement stmt = con
					.prepareStatement(selectAllSectionInfoSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Name_English.put(rs.getString("Local_Code"),
						rs.getString("Name_English"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Name_English;
	}

}
