package com.download.historicaldatadownload.yahoo.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SectionDao {
	
	private static HashMap<String, String> sectionInfo = new HashMap<>();

	public SectionDao() {
		// TODO Auto-generated constructor stub
	}
	
	public SectionDao(Connection con) {
		
		String selectAllSectionInfoSql = "Select Local_Code, 33_Sector_name FROM Section_Tokyo";
		try {
			PreparedStatement stmt = con.prepareStatement(selectAllSectionInfoSql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				getSectionInfo().put(rs.getString("Local_Code"), rs.getString("33_Sector_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> getSectionInfo() {
		return sectionInfo;
	}

	public static void setSectionInfo(HashMap<String, String> sectionInfo) {
		SectionDao.sectionInfo = sectionInfo;
	}

}
