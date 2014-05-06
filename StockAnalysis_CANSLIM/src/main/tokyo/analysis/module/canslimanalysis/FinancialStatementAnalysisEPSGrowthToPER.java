package module.canslimanalysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import namespace.DBNameSpace;
import jdbcdao.SingleItemDaoFromDB;

public class FinancialStatementAnalysisEPSGrowthToPER {

	private static final String getPER = "SELECT AdjClose FROM ?_HistoricalQuotes_Tokyo Where "
			+ "Date = (SELECT MAX(Date) FROM ?_HistoricalQuotes_Tokyo)";
	private static final String getLatestYearNetIncome = "SELECT * FROM (SELECT Local_Code code,Net_Income, Fiscal_Year year FROM "
			+ "FinancialStatementTokyo_test WHERE "
			+ "Form = 'consolidate' ORDER BY Local_Code, Fiscal_Year DESC) AS A GROUP BY code";

	public FinancialStatementAnalysisEPSGrowthToPER() {

	}

	public static HashMap<String, FinancialStatementAnalysisRecord> getEPSGrowthToPER(
			HashMap<String, FinancialStatementAnalysisRecord> resultMap,
			Connection con) {

		HashMap<String, Double> price = null;
		price = getLatestPrice(con);
		HashMap<String, Double> shareHolding = null;
		shareHolding = getShareHolding(con);
		HashMap<String, Double> eps = null;
		eps = getEPS(con);
		HashMap<String, Double> epsGrowth = null;
		epsGrowth = getEPSGrowth(con);
		HashMap<String, Double> per = null;
		per = getPER(price, eps);
		HashMap<String, Double> EPSGrwothToPER = null;
		EPSGrwothToPER = getEPSGrowthToPERRate(epsGrowth, per);
		
		resultMap = insertIntoMap(EPSGrwothToPER);
		return resultMap;
	}

	public static HashMap<String, Double> getLatestPrice(Connection con) {
		// TODO
		return null;
	}

	public static HashMap<String, Double> getShareHolding(Connection con) {
		// TODO
		return null;
	}

	public static HashMap<String, Double> getEPS(Connection con) {
		// TODO
		return null;
	}

	public static HashMap<String, Double> getEPSGrowth(Connection con) {
		// TODO
		return null;
	}

	public static HashMap<String, Double> getPER(HashMap<String, Double> price,
			HashMap<String, Double> EPS) {
		// TODO
		return null;
	}

	public static HashMap<String, Double> getEPSGrowthToPERRate(
			HashMap<String, Double> epsGrowthMap, HashMap<String, Double> per) {
		// TODO
		return null;
	}
	
	public static HashMap<String, FinancialStatementAnalysisRecord> insertIntoMap(HashMap<String, Double> EPSGrwothToPER) {
		//TODO
		return null;
	}
}
