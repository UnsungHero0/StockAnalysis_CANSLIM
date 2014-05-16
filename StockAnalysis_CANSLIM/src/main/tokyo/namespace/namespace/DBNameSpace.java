package namespace;

public class DBNameSpace {
	
	private static final String StockHistoricalPrice_DB = "_HistoricalQuotes_Tokyo";
	private static final String FinancailStatement_DB = "FinancialStatementTokyo_test";
	private static final String Section_DB = "Section_Tokyo";
	private static final String StockTokyo_DB = "Stock_Tokyo";
	private static final String QuarterFinancialStatement_DB = "QuarterFinancialStatementTokyo_test";
	private static final String QuarterFinancialStatementAnalysis_DB = "QuarterFinancialStatementAnalysisTokyo_test";
	private static final String HistoricalstocksplitDb = "HistoricalStockSplit_test";
	private static final String ShareHolding_DB = "ShareHolding_test";
	private static final String ListedCompanies_Tokyo_DB = "ListedCompaniesTokyo";

	public DBNameSpace() {
		// TODO Auto-generated constructor stub
	}

	public static String getStockhistoricalpriceDb() {
		return StockHistoricalPrice_DB;
	}

	public static String getFinancailstatementDb() {
		return FinancailStatement_DB;
	}

	public static String getSectionDb() {
		return Section_DB;
	}

	public static String getStocktokyoDb() {
		return StockTokyo_DB;
	}

	public static String getQuarterfinancialstatementDb() {
		return QuarterFinancialStatement_DB;
	}

	public static String getQuarterfinancialstatementanalysisDb() {
		return QuarterFinancialStatementAnalysis_DB;
	}

	public static String getHistoricalstocksplitDb() {
		return HistoricalstocksplitDb;
	}

	public static String getHistoricalstocksplitdb() {
		return HistoricalstocksplitDb;
	}

	public static String getShareholdingDb() {
		return ShareHolding_DB;
	}

	public static String getListedcompaniesTokyoDb() {
		return ListedCompanies_Tokyo_DB;
	}
	

}
