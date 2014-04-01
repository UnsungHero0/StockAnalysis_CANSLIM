package com.analysis.financialstatementanalysis.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.download.historicaldatadownload.yahoo.jdbc.DataSourceUtil;
import com.download.historicaldatadownload.yahoo.jdbc.dao.CodeListsDao;
import com.download.historicaldatadownload.yahoo.jdbc.dao.SectionDao;

public class FinancialStatementAnalysisImpl {

	public FinancialStatementAnalysisImpl() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {

		ArrayList<String> codeList = new CodeListsDao()
				.getCodeListsFromFinancialStatement();
		HashMap<String, FinancialStatementAnalysisRecord> resultMap = new HashMap<>();
		Connection con = null;

		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			new SectionDao(con);
			HashMap<String, String> section = SectionDao.getSectionInfo();
			for (String code : codeList) {
				FinancialStatementAnalysisRecord record = new FinancialStatementAnalysisRecord();
				record.setLocal_Code(code);
				record.setSector_Name(section.get(code));
				resultMap.put(code, record);
			}
			
			resultMap = new FinancialStatementAnalysisEPSBPS().getEPSAndBPS(
					resultMap, con);
			resultMap = new FinancialStatementAnalysisRSI().calculateRSI(
					resultMap, con);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		ArrayList<Entry<String, FinancialStatementAnalysisRecord>> resultEntrySet = new ArrayList<>(
				resultMap.entrySet());
		System.out.println(resultEntrySet.size());
		System.out.println(resultEntrySet.get(0).getValue().getLocal_Code());

		Collections
				.sort(resultEntrySet,
						new Comparator<Entry<String, FinancialStatementAnalysisRecord>>() {
							public int compare(
									Entry<String, FinancialStatementAnalysisRecord> o1,
									Entry<String, FinancialStatementAnalysisRecord> o2) {
								return o2
										.getValue()
										.getePSAverageGrowthRate()
										.compareTo(
												o1.getValue()
														.getePSAverageGrowthRate());
							}
						});
		Collections
		.sort(resultEntrySet,
				new Comparator<Entry<String, FinancialStatementAnalysisRecord>>() {
					public int compare(
							Entry<String, FinancialStatementAnalysisRecord> o1,
							Entry<String, FinancialStatementAnalysisRecord> o2) {
						return o2
								.getValue()
								.getSector_Name()
								.compareTo(
										o1.getValue()
												.getSector_Name());
					}
				});
		System.out.println(resultEntrySet.get(0).getValue().getLocal_Code());

		for (Entry<String, FinancialStatementAnalysisRecord> record : resultEntrySet) {
			if (record.getValue().getePSAverageGrowthRate() >= 0.25
					&& record.getValue().getRSIInAllStock() >= 80) {
				boolean ifqualified = true;
				for (Float value : record.getValue().getePSArray()) {
					if (value < 0.20f) {
						ifqualified = false;
						break;
					}
				}
				if (ifqualified == true)
					System.out.print(record.getValue().getLocal_Code()
							+ "  "
							+ record.getValue().getSector_Name()
							+ "  "
							+ (int) (record.getValue()
									.getePSAverageGrowthRate() * 100)
							+ "%  "
							+ (int) (record.getValue()
									.getbPSAverageGrowthRate() * 100) + "%  "
							+ record.getValue().getRSIInAllStock().intValue()
							+ "\n");
			}
		}

	}

	public static DataSource getDataSource() {
		return DataSourceUtil.getTokyoDataSourceRoot();
	}

}
