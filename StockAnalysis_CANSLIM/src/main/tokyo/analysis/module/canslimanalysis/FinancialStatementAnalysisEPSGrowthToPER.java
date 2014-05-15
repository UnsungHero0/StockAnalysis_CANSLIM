package module.canslimanalysis;

/**
 * EPSGrowthToPERGrowth
 * Since it is not decided how to calculate the PERGrowth
 * So this is used to compute the ratio of Annual EPSGrowth to PER
 * 
 * step 1. fetching Net_Income from DB of no more than last 3 years
 * step 2. calculate the growth rate of Net_Income
 * step 2. calculate the EPS (latest Net_Income / shareHolding)
 * step 2. calculate the real time PER ( price / EPS )
 * step 4. Net_Income growth rate / PER ( = EPS growth rate / PER)
 * 
 * higher calculated value shows higher potential
 * 
 * @author Daytona
 */import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import commontool.JDBCUtil;
import namespace.DBNameSpace;


public class FinancialStatementAnalysisEPSGrowthToPER {

	private static ArrayList<String> codeList = null;
	private static final String GETNETINCOME = "SELECT Local_Code, Net_Income FROM "
			+ DBNameSpace.getFinancailstatementDb()
			+ " WHERE form = 'consolidate' ORDER BY Local_Code, Fiscal_Year DESC";
	private static final String GETLATESTPRICE = "SELECT date,AdjClose FROM ?" + DBNameSpace.getStockhistoricalpriceDb() + " ORDER BY "
			+ "Date DESC LIMIT 1";
	private static final String GETSHAREHOLDING = "SELECT * FROM " + DBNameSpace.getShareholdingDb();

	public FinancialStatementAnalysisEPSGrowthToPER() {

	}

	public static HashMap<String, FinancialStatementAnalysisRecord> getEPSGrowthToPER(
			HashMap<String, FinancialStatementAnalysisRecord> resultMap,
			Connection con) {
		System.out.println("start EPSGrowthToPER");

		// the consequence is from the latest year to older year
		codeList = new ArrayList<>(resultMap.keySet());
		HashMap<String, ArrayList<Double>> netIncomeList = getNetIncomeList(con);
		HashMap<String, Double> netIncomeGrowthRate = getNetIncomeGrowthRate(netIncomeList);
		HashMap<String, Double> realTimePER = getRealTimePER(con);
		HashMap<String, Double> EPSGrowthToPER = getEPSGrowthToPER(
				netIncomeGrowthRate, realTimePER);
		
		System.out.println("netIncomegrowthRate" + netIncomeGrowthRate.get("7760") + "  " 
		+ " PER " + realTimePER.get("7760") + " EPS TO PER " + EPSGrowthToPER.get("7760"));

		System.out.println("finish EPSGrowthToPER");

		return insertIntoRecord(resultMap, EPSGrowthToPER);
	}

	public static HashMap<String, ArrayList<Double>> getNetIncomeList(
			Connection con) {

		HashMap<String, ArrayList<Double>> result = new HashMap<>();

		ResultSet rs = JDBCUtil.excuteQueryWithResult(GETNETINCOME, con);
		try {
			while (rs.next()) {
				String code = rs.getString("Local_Code");
				Double netIncome = rs.getDouble("Net_Income");
				if (result.containsKey(code)) {
					if (result.get(code).size() < 3) {
						result.get(code).add(netIncome);
					}
				} else if (codeList.contains(code)) {
					ArrayList<Double> newList = new ArrayList<>();
					newList.add(netIncome);
					result.put(code, newList);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static HashMap<String, Double> getNetIncomeGrowthRate(
			HashMap<String, ArrayList<Double>> input) {
		HashMap<String, Double> result = new HashMap<>();
		for (String code : input.keySet()) {
			if (input.get(code).size() > 1) {
				result.put(code, calculateGrowthRate(input.get(code)));
			} else {
				codeList.remove(code);
			}
		}
		return result;
	}

	public static Double calculateGrowthRate(ArrayList<Double> input) {
		Double result = 0.0;
		for (int i = 0; i < input.size() - 1; i++) {
			result += (input.get(i) - input.get(i + 1))
					/ Math.abs(input.get(i + 1));
		}
		return result / Double.valueOf((input.size() - 1));
	}

	public static HashMap<String, Double> getRealTimePER(Connection con) {
		HashMap<String, Double> PER = new HashMap<>();
		// PER = price / EPS
		HashMap<String, Double> realTimeEPS = getRealTimeEPS(con);
		HashMap<String, Double> price = getPrice(con);

		for (String code : codeList) {
			if (realTimeEPS.containsKey(code) && price.containsKey(code)) {
				PER.put(code, price.get(code) / realTimeEPS.get(code));
			}
		}
		return PER;
	}

	public static HashMap<String, Double> getRealTimeEPS(Connection con) {
		HashMap<String, Double> EPS = new HashMap<>();
		HashMap<String, Double> netIncome = getNetIncome(con);
		HashMap<String, Double> shareHolding = getShareHolding(con);

		for (String code : codeList) {
			if (netIncome.containsKey(code) && shareHolding.containsKey(code)) {
				EPS.put(code, netIncome.get(code) / shareHolding.get(code));
			}
		}
		return EPS;
	}

	public static HashMap<String, Double> getNetIncome(Connection con) {
		HashMap<String, Double> netIncome = new HashMap<>();
		ResultSet rs = JDBCUtil.excuteQueryWithResult(GETNETINCOME, con);
		try {
			while (rs.next()) {
				String code = rs.getString("Local_code");
				Double netIncomeRs = rs.getDouble("Net_Income");
				if ((!netIncome.containsKey(code)) && codeList.contains(code)) {
					netIncome.put(code, netIncomeRs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return netIncome;
	}

	public static HashMap<String, Double> getPrice(Connection con) {
		HashMap<String, Double> price = new HashMap<>();
		try {
			for(String code : codeList) {
				java.sql.PreparedStatement stmt = con.prepareStatement(GETLATESTPRICE);
				stmt.setInt(1,Integer.valueOf(code));
				ResultSet rs = JDBCUtil.excuteQueryWithResult(stmt);
				if(rs.next()) {
					price.put(code, rs.getDouble("AdjClose"));
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return price;
	}

	public static HashMap<String, Double> getShareHolding(Connection con) {
		HashMap<String, Double> result = new HashMap<>();
		try {
			ResultSet rs = JDBCUtil.excuteQueryWithResult(GETSHAREHOLDING, con);
			while(rs.next()) {
				String code = rs.getString("Local_Code");
				if(codeList.contains(code)) {
					result.put(code, rs.getDouble("ShareHolding"));
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static HashMap<String, Double> getEPSGrowthToPER(
			HashMap<String, Double> EPSGrwothRate, HashMap<String, Double> PER) {
		HashMap<String, Double> result = new HashMap<>();
		for(String code : codeList) {
			if(EPSGrwothRate.containsKey(code) && PER.containsKey(code)) {
				if(EPSGrwothRate.get(code) > 0 && PER.get(code) > 0) {
				result.put(code, EPSGrwothRate.get(code) / PER.get(code));
				} else {
					result.put(code, -1.0 * Math.abs(EPSGrwothRate.get(code) / PER.get(code)));
				}
			}
		}
		return result;
	}

	public static HashMap<String, FinancialStatementAnalysisRecord> insertIntoRecord(
			HashMap<String, FinancialStatementAnalysisRecord> resultMap,
			HashMap<String, Double> EPSGrowthToPER) {
		for(String code : codeList) {
			if(resultMap.containsKey(code) && EPSGrowthToPER.containsKey(code)) {
				resultMap.get(code).setePSAveGrowthRateToPER(EPSGrowthToPER.get(code).floatValue());
			}
		}
		return resultMap;
	}

}
