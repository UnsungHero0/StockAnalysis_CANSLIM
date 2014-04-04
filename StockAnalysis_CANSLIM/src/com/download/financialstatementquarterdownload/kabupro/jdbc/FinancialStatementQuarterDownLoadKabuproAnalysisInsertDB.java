package com.download.financialstatementquarterdownload.kabupro.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.namespace.DBNameSpace;

public class FinancialStatementQuarterDownLoadKabuproAnalysisInsertDB {

	public FinancialStatementQuarterDownLoadKabuproAnalysisInsertDB() {
		// TODO Auto-generated constructor stub
	}

	public static void insetIntoDB(
			HashMap<String, FinancialStatementQuarterDownLoadKabuproAnalysisRecord> result,
			Connection con) {
		Set<String> key = result.keySet();
		for (String code : key) {
			insertRecordIntoDB(result.get(code), con);
		}
		// TODO
	}

	public static void insertRecordIntoDB(FinancialStatementQuarterDownLoadKabuproAnalysisRecord record, Connection con) {
		/*
		String Analysis_Type[] = {"Accumulation_Income","Accumulation_Income_Increasement_Percent_Year",
				"Season_Income","Season_Income_Increasement_Percent_Season","Season_Income_Increasement_Percent_Year"};
				*/
		String field = record.getAccumulation_IncomeFieldValue()[0];
		String value = record.getAccumulation_IncomeFieldValue()[1];
		try {
			String sql = "INSERT INTO "+ DBNameSpace.getQuarterfinancialstatementanalysisDb() + " "
				+ field
				+ " VALUES "
				+ value;
			System.out.println(sql);
			con.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		field = record.getAccumulation_Income_Increasement_Percent_YearFieldValue()[0];
		value = record.getAccumulation_Income_Increasement_Percent_YearFieldValue()[1];
		try {
			String sql = "INSERT INTO "+ DBNameSpace.getQuarterfinancialstatementanalysisDb() + " "
				+ field
				+ " VALUES "
				+ value;
			System.out.println(sql);
			con.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		field = record.getSeason_IncomeFieldValue()[0];
		value = record.getSeason_IncomeFieldValue()[1];
		try {
			String sql = "INSERT INTO "+ DBNameSpace.getQuarterfinancialstatementanalysisDb() + " "
				+ field
				+ " VALUES "
				+ value;
			System.out.println(sql);
			con.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		field = record.getSeason_Income_Increasement_Percent_SeasonFieldValue()[0];
		value = record.getSeason_Income_Increasement_Percent_SeasonFieldValue()[1];
		try {
			String sql = "INSERT INTO "+ DBNameSpace.getQuarterfinancialstatementanalysisDb() + " "
				+ field
				+ " VALUES "
				+ value;
			System.out.println(sql);
			con.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		field = record.getSeason_Income_Increasement_Percent_YearFieldValue()[0];
		value = record.getSeason_Income_Increasement_Percent_YearFieldValue()[1];
		try {
			String sql = "INSERT INTO "+ DBNameSpace.getQuarterfinancialstatementanalysisDb() + " "
				+ field
				+ " VALUES "
				+ value;
			System.out.println(sql);
			con.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*
		for (String type : Analysis_Type) {
			Method m = null;
			for(Method method : record.getClass().getMethods()) {
				if (method.getName().contains(type) && method.getName().contains("Value")) {
					m = method;
					break;
				}
			}
			System.out.println(m.getName());
			try {
				String fieldValue[] = (String[]) m.invoke(null,null);
				String field = fieldValue[0];
				String value = fieldValue[1];
				String sql = "INSERT INTO "+ DBNameSpace.getQuarterfinancialstatementanalysisDb() + " "
					+ field
					+ " VALUES "
					+ value;
				con.prepareStatement(sql).execute();
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		*/
		//TODO
	}
}
