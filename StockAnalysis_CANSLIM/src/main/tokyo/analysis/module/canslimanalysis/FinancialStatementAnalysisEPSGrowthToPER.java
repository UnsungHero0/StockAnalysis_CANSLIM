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

	public static FinancialStatementAnalysisRecord getEPSGrowthToPER(
			HashMap<String, FinancialStatementAnalysisRecord> resultMap,
			Connection con) {

		HashMap<String, Float> price = null;
		ResultSet rs = SingleItemDaoFromDB.selectFromDBOrder("Adj_Close",
				DBNameSpace.getFinancailstatementDb(),
				"Local_Code, Fiscal_Year", con);

		// TODO
		return null;
	}

}
