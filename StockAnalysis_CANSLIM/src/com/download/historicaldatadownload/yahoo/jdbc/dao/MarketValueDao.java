package com.download.historicaldatadownload.yahoo.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.download.historicaldatadownload.yahoo.jdbc.DataSourceUtil;

public class MarketValueDao {

	public MarketValueDao() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		Connection con = null;
		CodeListsDao cld = new CodeListsDao();
		ArrayList<String> codeList = cld.getCodeListsFromFinancialStatement();
		// System.out.println("codelist size " + codeList.size());
		MarketValueDao self = new MarketValueDao();
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			for (int i = 0; i < 12; i++) {
				Double result1 = self.getMarketValue(codeList, i, con);
				// System.out.println(result1);
			}
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

	}

	public Double getMarketValue(ArrayList<String> codeList, Integer time,
			Connection con) {
		Double result = 0.0;
		// getPrice
		HashMap<String, Double> price = new HashMap<>();
		price = getPrice(codeList, time, con);
		// getStockShare
		HashMap<String, Double> stockShare = new HashMap<>();
		stockShare = getStockShare(codeList, con);
		Set<String> keySet1 = price.keySet();
		Set<String> keySet2 = stockShare.keySet();
		ArrayList<String> newList = new ArrayList<>();
		for (String key : keySet1) {
			if (keySet2.contains(key)) {
				newList.add(key);
			}
		}
		// System.out.println("size of price " + price.keySet().size());
		// System.out.println("size of stockShare "
		// + stockShare.keySet().size());
		for (String key : newList) {
			result = result + price.get(key) * stockShare.get(key);
		}
		
		return result;
	}
	
	

	public Float getMarketIndex(ArrayList<String> codeList, Integer time,
			Connection con) {

		Double result = 0.0;
		// getPrice
		HashMap<String, Double> price = new HashMap<>();
		price = getPrice(codeList, time, con);
		// getStockShare
		HashMap<String, Double> stockShare = new HashMap<>();
		stockShare = getStockShare(codeList, con);
		Set<String> keySet1 = price.keySet();
		Set<String> keySet2 = stockShare.keySet();
		ArrayList<String> newList = new ArrayList<>();
		for (String key : keySet1) {
			if (keySet2.contains(key)) {
				newList.add(key);
			}
		}
		// System.out.println("size of price " + price.keySet().size());
		// System.out.println("size of stockShare "
		// + stockShare.keySet().size());
		for (String key : newList) {
			result = result + price.get(key) * stockShare.get(key);
		}

		Double totalStockShare = getTotalStockShare(newList, con);
		return (float) (result / totalStockShare);
	}

	public HashMap<String, Double> getPrice(ArrayList<String> codeList,
			Integer time, Connection con) {

		HashMap<String, Double> result = new HashMap<>();
		String selectPrice = "SELECT AdjClose FROM "
				+ "?_HistoricalQuotes_Tokyo WHERE "
				+ "Date >= date_sub((SELECT MAX(date) FROM ?_HistoricalQuotes_Tokyo),interval +? month) "
				+ "ORDER BY Date";
		try {
			for (String code : codeList) {
				PreparedStatement stmt = con.prepareStatement(selectPrice);
				stmt.setInt(1, Integer.valueOf(code));
				stmt.setInt(2, Integer.valueOf(code));
				stmt.setInt(3, time);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() == true) {
					result.put(code, rs.getDouble("AdjClose"));
				}
				rs.close();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public HashMap<String, Double> getStockShare(ArrayList<String> codeList,
			Connection con) {
		String codeListString = "(";
		for (String codeString : codeList) {
			codeListString = codeListString + codeString + ", ";
		}
		codeListString = codeListString.substring(0,
				codeListString.length() - 2) + ")";
		HashMap<String, Double> result = new HashMap<>();
		String selectPrice = "SELECT Local_Code, Outstanding_shares, MAX(Fiscal_Year) FROM "
				+ "FinancialStatementTokyo_test WHERE "
				+ "Form ='independent' AND Local_Code IN "
				+ codeListString
				+ " GROUP BY Local_Code";
		try {
			PreparedStatement stmt = con.prepareStatement(selectPrice);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.put(rs.getString("Local_Code"),
						rs.getDouble("Outstanding_shares"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Double getTotalStockShare(ArrayList<String> codeList, Connection con) {
		Double result = 0.0;
		String codeListString = "(";
		for (String codeString : codeList) {
			codeListString = codeListString + codeString + ", ";
		}
		codeListString = codeListString.substring(0,
				codeListString.length() - 2) + ")";
		String selectPrice = "SELECT Local_Code, Outstanding_shares, MAX(Fiscal_Year) FROM "
				+ "FinancialStatementTokyo_test WHERE "
				+ "Form ='independent' AND Local_Code IN "
				+ codeListString
				+ " GROUP BY Local_Code";
		try {
			PreparedStatement stmt = con.prepareStatement(selectPrice);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result = result + rs.getDouble("Outstanding_shares");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
