package namespace;

public class ShanghaiDBNameSpace {

	private static final String SCHEMA_DB = "Shanghaiexchange.";
	private static final String FutureExchangeHistoricalQuote_DB = "FutureExchangeHistoricalQuote_Shanghai";
	private static final String STOCKHISTORICALPRICE_DB = "_HistoricalQuotes_Shanghai";
	private static final String FinancailStatement_DB = "Shanghaiexchange.FinancialStatement_Shanghai";
	private static final String IncomeStatement_DB = "Shanghaiexchange.IncomeStatement_Shanghai";
	private static final String Section_DB = "Shanghaiexchange.Section_Shanghai";
	private static final String StockShanghai_DB = "Shanghaiexchange.Stock_Shanghai";
	private static final String QuarterFinancialStatement_DB = "Shanghaiexchange.QuarterFinancialStatementShanghai_Shanghai";
	private static final String QuarterFinancialStatementAnalysis_DB = "Shanghaiexchange.QuarterFinancialStatementAnalysisShanghai_Shanghai";
	private static final String HistoricalstocksplitDb = "Shanghaiexchange.HistoricalStockSplit_Shanghai";
	private static final String ShareHolding_DB = "Shanghaiexchange.ShareHolding_Shanghai";
	private static final String LISTEDCOMPANIES_DB = "Shanghaiexchange.listedcompanies_Shanghai";
	private static final String SectorAnalysis_DB = "Shanghaiexchange.SectorAnalysis_Shanghai";
	private static final String STOCKINDEXFUTUREVOLUMERANK_DB = "Shanghaiexchange.StockIndexFutureVolumeRank_Shanghai";
	
	public ShanghaiDBNameSpace() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getFutureexchangehistoricalquoteDb() {
		return getSchemaDb()+FutureExchangeHistoricalQuote_DB;
	}

	public static String getStockshanghaiDb() {
		return getSchemaDb()+StockShanghai_DB;
	}

	public static String getStockhistoricalpriceDb() {
		return STOCKHISTORICALPRICE_DB;
	}

	public static String getFinancailstatementDb() {
		return getSchemaDb()+FinancailStatement_DB;
	}

	public static String getSectionDb() {
		return getSchemaDb()+Section_DB;
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

	public static String getStockShanghaiDb() {
		return StockShanghai_DB;
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

	public static String getListedcompaniesShanghaiDb() {
		return LISTEDCOMPANIES_DB;
	}

	public static String getSectoranalysisDb() {
		return SectorAnalysis_DB;
	}

	public static String getSchemaDb() {
		return SCHEMA_DB;
	}

	public static String getStockindexfuturevolumerankDb() {
		return STOCKINDEXFUTUREVOLUMERANK_DB;
	}

}

