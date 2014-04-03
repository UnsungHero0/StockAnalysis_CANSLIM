package com.analysis.financialstatementanalysis.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.sql.DataSource;

import com.download.historicaldatadownload.yahoo.jdbc.DataSourceUtil;

public class FinancialStatementAnalysisYearlyGrowthRate {

	public FinancialStatementAnalysisYearlyGrowthRate() {
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, FinancialStatementAnalysisRecord> getGrowthRate(
			HashMap<String, FinancialStatementAnalysisRecord> record,
			String item, String form, Connection con) {
		// getcodelist from financialstatement
		// eps into arraylist
		// calculate the growthrate
		System.out.println(item + " start : ");
		Set<String> keySet = record.keySet();
		Integer allRecordNumber = keySet.size();
		Integer count = 0;
		Float percente = (float) 0;
		Integer i = 1;
		for (String code : keySet) {
			record.get(code).setForm(form);
			ArrayList<Float> rawArray = new ArrayList<>();
			rawArray = getRawArray(Integer.valueOf(code), form, item, con);
			if (rawArray.size() >= 2) {
				try {
					String setArrayMethodName = ("set"
							+ item + "Array")
							.toUpperCase();
					Method m = null;
					for (Method method : record.get(code).getClass().getDeclaredMethods()) {
						if (method.toString().toUpperCase()
								.contains(setArrayMethodName)) {
							m = method;
						}
					}
					m.invoke(record.get(code), rawArray);
					
					Float averageRate = getAverageGrowthRate(rawArray);
					String setAverageGrowthRateName = ("set"
							+ item + "AverageGrowthRate")
							.toUpperCase();
					for (Method method : record.get(code).getClass().getDeclaredMethods()) {
						if (method.toString().toUpperCase()
								.contains(setAverageGrowthRateName)) {
							m = method;
						}
					}
					m.invoke(record.get(code), averageRate);
					
					String setGrowthRateArrayName = ("set"
							+ item + "GrowthRateArray")
							.toUpperCase();
					for (Method method : record.get(code).getClass().getDeclaredMethods()) {
						if (method.toString().toUpperCase()
								.contains(setGrowthRateArrayName)) {
							m = method;
						}
					}
					m.invoke(record.get(code), getGrowthRateArray(rawArray));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			percente = ((float) count++ / (float) allRecordNumber);
			if (percente * 10 > 1 * i) {
				System.out.print((int) (percente * 100) + "%-> ");
				i++;
			}
		}
		System.out.println("100%");

		return record;
	}

	public static ArrayList<Float> getRawArray(Integer code, String form,
			String item, Connection con) {
		ArrayList<Float> result = new ArrayList<>();
		try {
			String selectRecord = "SELECT Fiscal_Year, " + item
					+ " FROM FinancialStatementTokyo_test WHERE "
					+ "Local_Code = " + code + " AND Form = " + "'" + form
					+ "' ORDER BY Fiscal_Year ASC";
			ResultSet rs = con.prepareStatement(selectRecord).executeQuery();
			while (rs.next()) {
				result.add(rs.getFloat(item));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<Float> getGrowthRateArray(ArrayList<Float> input) {
		ArrayList<Float> result = new ArrayList<>();
		Float rate = (float) 0;
		for (int i = 0; i < input.size() - 1; i++) {
			if (input.get(i) != 0)
				rate = ((input.get(i + 1) - input.get(i)) / Math.abs(input
						.get(i)));
			result.add(rate);
		}
		return result;
	}

	public static Float getAverageGrowthRate(ArrayList<Float> input) {
		Float rate = (float) 0;
		for (int i = 0; i < input.size() - 1; i++) {
			if (input.get(i) != 0)
				rate = rate + (input.get(i + 1) - input.get(i))
						/ Math.abs(input.get(i));
		}
		rate = rate / (input.size() - 1);
		return rate;
	}

	public static Boolean satisfyCANSLIM(FinancialStatementAnalysisRecord record) {
		Integer count = 0;
		for (int i = 0; i < record.getePSGrowthRateArray().size(); i++) {
			if (record.getePSGrowthRateArray().get(i) > 0.25) {
				count++;
			}
		}
		if (count == record.getePSGrowthRateArray().size()
				&& record.getePSArray().get(record.getePSArray().size() - 1) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static DataSource getDataSource() {
		return DataSourceUtil.getTokyoDataSourceRoot();
	}
}
