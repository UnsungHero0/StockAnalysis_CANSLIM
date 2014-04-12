package com.analysis.financialstatementanalysis.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class FinancialStatementAnalysisVolume {

	private static final String getFifityWeeksAverageVolume = "SELECT AVG(Volume) result FROM ?_HistoricalQuotes_Tokyo WHERE "
			+ "Local_Code = ? AND Date >= date_sub((SELECT MAX(date) FROM ?_HistoricalQuotes_Tokyo),interval 50 week)";
	private static final String getTodayVolume = "SELECT Volume result FROM ?_HistoricalQuotes_Tokyo WHERE "
			+ "Date = (SELECT MAX(date) FROM ?_HistoricalQuotes_Tokyo)";

	public FinancialStatementAnalysisVolume() {

	}

	public static HashMap<String, FinancialStatementAnalysisRecord> getVolumeInfo(HashMap<String, FinancialStatementAnalysisRecord> record, Connection con) {
		System.out.println("Volume" + " start : ");
		Float percente = (float) 0;
		Integer i = 1;
		ArrayList<String> codeList = new ArrayList<>(record.keySet());
		for (String code : codeList) {
			FinancialStatementAnalysisRecord tempRecord = record.get(code);
			tempRecord = calculateVolume(record.get(code), con);
			record.put(code, tempRecord);
			
			percente = ((float) codeList.indexOf(code) / (float) codeList.size());
			if (percente * 10 > 1 * i) {
				System.out.print((int) (percente * 100) + "%-> ");
				i++;
			}
		}
		System.out.println("100%");
		return record;
	}

	public static FinancialStatementAnalysisRecord calculateVolume(
			FinancialStatementAnalysisRecord record, Connection con) {
		try {
			PreparedStatement stmt = con
					.prepareStatement(getFifityWeeksAverageVolume);
			stmt.setInt(1, Integer.valueOf(record.getLocal_Code()));
			stmt.setInt(2, Integer.valueOf(record.getLocal_Code()));
			stmt.setInt(3, Integer.valueOf(record.getLocal_Code()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				record.setFiftyWeekAverageVolume(rs.getDouble("result"));
			}
			stmt = con.prepareStatement(getTodayVolume);
			stmt.setInt(1, Integer.valueOf(record.getLocal_Code()));
			stmt.setInt(2, Integer.valueOf(record.getLocal_Code()));
			rs = stmt.executeQuery();
			if (rs.next()) {
				record.setTodayToFiftyWeeksAverage((float) ((rs
						.getDouble("result") - record
						.getFiftyWeekAverageVolume())/ record
						.getFiftyWeekAverageVolume()));
				/*
				if (record.getTodayToFiftyWeeksAverage() > 0.7)
				System.out.println(record.getLocal_Code() + "  " + record.getFiftyWeekAverageVolume() + "  " + rs
						.getDouble("result") + "  "+ record.getTodayToFiftyWeeksAverage());
						*/

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return record;
	}

}
