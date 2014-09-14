package namespace;

public class NewYorkDBNameSpace {
	private static final String SCHEMA_DB = "newyorkexchange.";
	private static final String STOCKHISTORICALPRICE_DB = "_HistoricalQuotes_newyork";
	private static final String FinancailStatement_DB = "newyorkexchange.FinancialStatement_newyork";
	private static final String IncomeStatement_DB = "newyorkexchange.IncomeStatement_newyork";
	public static String getIncomestatementDb() {
		return IncomeStatement_DB;
	}

	public static String getListedcompaniesDb() {
		return LISTEDCOMPANIES_DB;
	}

	private static final String Section_DB = "newyorkexchange.Section_newyork";
	private static final String Stocknewyork_DB = "newyorkexchange.Stock_newyork";
	private static final String QuarterFinancialStatement_DB = "newyorkexchange.QuarterFinancialStatementnewyork_newyork";
	private static final String QuarterFinancialStatementAnalysis_DB = "newyorkexchange.QuarterFinancialStatementAnalysisnewyork_newyork";
	private static final String HistoricalstocksplitDb = "newyorkexchange.HistoricalStockSplit_newyork";
	private static final String ShareHolding_DB = "newyorkexchange.ShareHolding_newyork";
	private static final String LISTEDCOMPANIES_DB = "newyorkexchange.listedcompanies_newyork";
	private static final String SectorAnalysis_DB = "newyorkexchange.SectorAnalysis_newyork";

	public NewYorkDBNameSpace() {
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

	public static String getStocknewyorkDb() {
		return Stocknewyork_DB;
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

	public static String getListedcompaniesNewYorkDb() {
		return LISTEDCOMPANIES_DB;
	}

	public static String getSectoranalysisDb() {
		return SectorAnalysis_DB;
	}

	public static String getSchemaDb() {
		return SCHEMA_DB;
	}
}
