package com.analysis.financialstatementanalysis.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class FinancialStatementAnalysisRSI {

	private static Float seasonWeight[] = { 0.4f, 02f, 02f, 02f };

	public FinancialStatementAnalysisRSI() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * public static void main(String args[]) { CodeListsDao cld = new
	 * CodeListsDao(); ArrayList<String> codeList =
	 * cld.getCodeListsFromFinancialStatement();
	 * calculateRelativeStockPriceStrength(codeList); }
	 */

	/**
	 * 1. calculate the increase ratio 2. ranking
	 */
	public HashMap<String, FinancialStatementAnalysisRecord> calculateRSI(
			HashMap<String, FinancialStatementAnalysisRecord> resultMap,
			Connection con) {
		// for temp container
		HashMap<String, Float> resultHashMap = new HashMap<>();

		// initialize the monthArray
		// e.g. if startMonth = 3, endMonth = 0, means the duration from three
		// months before until 0 month before
		ArrayList<Integer> startMonthArray = new ArrayList<>();
		ArrayList<Integer> endMonthArray = new ArrayList<>();
		startMonthArray.add(3);
		endMonthArray.add(0);
		startMonthArray.add(6);
		endMonthArray.add(3);
		startMonthArray.add(9);
		endMonthArray.add(6);
		startMonthArray.add(12);
		endMonthArray.add(9);

		// stockShare = getStockShare(con);
		try {
			Set<String> keySet = resultMap.keySet();
			for (String code : keySet) {
				Float value = 0F;
				for (int i = 0; i < startMonthArray.size(); i++) {
					if ((getPriceChangePrecent(code, startMonthArray.get(i),
							endMonthArray.get(i), con)) != null)
						value = value
								+ getPriceChangePrecent(code,
										startMonthArray.get(i),
										endMonthArray.get(i), con)
								* seasonWeight[i];
				}
				resultHashMap.put(code, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, Float> RSI = turnToOneilRSI(resultHashMap);
		Set<String> setKey = RSI.keySet();
		for (String code : setKey) {
			resultMap.get(code).setRSIInAllStock(RSI.get(code));
			/*
			 * System.out.println(code + " " + RSI.get(code));
			 */
		}

		return resultMap;

	}
	
	public HashMap<String, FinancialStatementAnalysisSectionRecord> calculateSectionRSI(
			HashMap<String, FinancialStatementAnalysisSectionRecord> resultMap,
			Connection con) {
		// for temp container
		HashMap<String, Float> resultHashMap = new HashMap<>();

		// initialize the monthArray
		// e.g. if startMonth = 3, endMonth = 0, means the duration from three
		// months before until 0 month before
		ArrayList<Integer> startMonthArray = new ArrayList<>();
		ArrayList<Integer> endMonthArray = new ArrayList<>();
		startMonthArray.add(3);
		endMonthArray.add(0);
		startMonthArray.add(6);
		endMonthArray.add(3);
		startMonthArray.add(9);
		endMonthArray.add(6);
		startMonthArray.add(12);
		endMonthArray.add(9);

		// stockShare = getStockShare(con);
		try {
			Set<String> keySet = resultMap.keySet();
			for (String code : keySet) {
				Float value = 0F;
				for (int i = 0; i < startMonthArray.size(); i++) {
					if ((getPriceChangePrecent(code, startMonthArray.get(i),
							endMonthArray.get(i), con)) != null)
						value = value
								+ getPriceChangePrecent(code,
										startMonthArray.get(i),
										endMonthArray.get(i), con)
								* seasonWeight[i];
				}
				resultHashMap.put(code, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, Float> RSI = turnToOneilRSI(resultHashMap);
		Set<String> setKey = RSI.keySet();
		for (String code : setKey) {
			resultMap.get(code).setrSI(RSI.get(code));
			/*
			 * System.out.println(code + " " + RSI.get(code));
			 */
		}

		return resultMap;

	}

	public static Float getPriceChangePrecent(String code, Integer startMonth,
			Integer endMonth, Connection con) {
		Float result = 0f;
		String selectChangePrecent = "SELECT AdjClose FROM "
				+ "?_HistoricalQuotes_Tokyo WHERE "
				+ "Date >= date_sub((SELECT MAX(date) FROM ?_HistoricalQuotes_Tokyo),interval +? month) AND "
				+ "Date <= date_sub((SELECT MAX(date) FROM ?_HistoricalQuotes_Tokyo),interval +? month) "
				+ "ORDER BY Date";

		try {
			PreparedStatement stmt = con.prepareStatement(selectChangePrecent);
			stmt.setInt(1, Integer.valueOf(code));
			stmt.setInt(2, Integer.valueOf(code));
			stmt.setInt(3, startMonth);
			stmt.setInt(4, Integer.valueOf(code));
			stmt.setInt(5, endMonth);
			// System.out.println(stmt.toString());
			ResultSet rs = stmt.executeQuery();
			if (rs.next() == true) {
				Float first = rs.getFloat("AdjClose");
				rs.last();
				Float last = rs.getFloat("AdjClose");
				result = (last - first) / first;
				if (last == 0 || first == 0) {
					System.out.println(code + " " + startMonth + " " + first
							+ " " + last);
				}
			} else {
				result = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}

	public static Float findMax(ArrayList<Float> input) {
		Collections.sort(input, new Comparator<Float>() {
			@Override
			public int compare(Float o1, Float o2) {
				return o2.compareTo(o1);
			}
		});
		return input.get(0);
	}

	public static Float findMin(ArrayList<Float> input) {
		Collections.sort(input, new Comparator<Float>() {
			@Override
			public int compare(Float o1, Float o2) {
				return o1.compareTo(o2);
			}
		});
		return input.get(0);
	}

	public static HashMap<String, Float> turnToOneilRSI(
			HashMap<String, Float> input) {

		ArrayList<Float> tempContainer = new ArrayList<>();
		Set<String> keySet = input.keySet();
		for (String code : keySet) {
			tempContainer.add(input.get(code));
			// System.out.println(input.get(code));
		}
		Collections.sort(tempContainer, new Comparator<Float>() {
			@Override
			public int compare(Float o1, Float o2) {
				return o1.compareTo(o2);
			}
		});

		for (String code : keySet) {
			Float rank = 1f + tempContainer.indexOf(input.get(code));
			Float value = 1 + ((rank / tempContainer.size()) * 98 / 100 * 100);
			input.put(code, value);
		}

		return input;
	}

}
