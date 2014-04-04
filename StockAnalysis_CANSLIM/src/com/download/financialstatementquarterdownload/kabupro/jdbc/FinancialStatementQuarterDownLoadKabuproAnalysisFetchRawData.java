package com.download.financialstatementquarterdownload.kabupro.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import com.namespace.DBNameSpace;

public class FinancialStatementQuarterDownLoadKabuproAnalysisFetchRawData {

	public FinancialStatementQuarterDownLoadKabuproAnalysisFetchRawData() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<FinancialStatementQuarterDownLoadKabuproAnalysisRecord> fetchRawDataFromDB(
			Connection con) {
		ArrayList<FinancialStatementQuarterDownLoadKabuproAnalysisRecord> result = new ArrayList<>();
		try {
			String sql = "Select * from "
					+ DBNameSpace.getQuarterfinancialstatementDb();
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			while (rs.next()) {
				result.add(getRecord(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static FinancialStatementQuarterDownLoadKabuproAnalysisRecord getRecord(
			ResultSet rs) {
		FinancialStatementQuarterDownLoadKabuproAnalysisRecord record = new FinancialStatementQuarterDownLoadKabuproAnalysisRecord();
		
		return record;
	}

}
