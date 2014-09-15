package namespace;

public class SydneyDBNameSpace {

	private static final String SCHEMA_DB = "sydneyexchange.";
	private static final String STOCKHISTORICALPRICE_DB = "_HistoricalQuotes_Sydney";
	private static final String FinancailStatement_DB = "sydneyexchange.FinancialStatement_Sydney";
	private static final String IncomeStatement_DB = "sydneyexchange.IncomeStatement_Sydney";
	private static final String Section_DB = "sydneyexchange.Section_Sydney";
	private static final String StockSydney_DB = "sydneyexchange.Stock_Sydney";
	private static final String QuarterFinancialStatement_DB = "sydneyexchange.QuarterFinancialStatementSydney_Sydney";
	private static final String QuarterFinancialStatementAnalysis_DB = "sydneyexchange.QuarterFinancialStatementAnalysisSydney_Sydney";
	private static final String HistoricalstocksplitDb = "sydneyexchange.HistoricalStockSplit_Sydney";
	private static final String ShareHolding_DB = "sydneyexchange.ShareHolding_Sydney";
	private static final String LISTEDCOMPANIES_DB = "sydneyexchange.listedcompanies_Sydney";
	private static final String SectorAnalysis_DB = "sydneyexchange.SectorAnalysis_Sydney";

	public SydneyDBNameSpace() {
		// TODO Auto-generated constructor stub
	}

	public static String getStockhistoricalpriceDb() {
		return STOCKHISTORICALPRICE_DB;
	}

	public static String getFinancailstatementDb() {
		return FinancailStatement_DB;
	}

	public static String getSectionDb() {
		return Section_DB;
	}

	public static String getStockSydneyDb() {
		return StockSydney_DB;
	}

	public static String getQuarterfinancialstatementDb() {
		return QuarterFinancialStatement_DB;
	}

	public static String getQuarterfinancialstatementanalysisDb() {
		return QuarterFinancialStatementAnalysis_DB;
	}
	
	public static String getIncomestatementDb() {
		return IncomeStatement_DB;
	}

	public static String getStocksydneyDb() {
		return StockSydney_DB;
	}

	public static String getListedcompaniesDb() {
		return LISTEDCOMPANIES_DB;
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

	public static String getListedcompaniesSydneyDb() {
		return LISTEDCOMPANIES_DB;
	}

	public static String getSectoranalysisDb() {
		return SectorAnalysis_DB;
	}

	public static String getSchemaDb() {
		return SCHEMA_DB;
	}

}
