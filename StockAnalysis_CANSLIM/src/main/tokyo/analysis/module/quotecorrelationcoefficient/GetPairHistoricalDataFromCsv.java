package module.quotecorrelationcoefficient;

import java.util.ArrayList;


public class GetPairHistoricalDataFromCsv {

	private StockPairDailyQuote pairQuote = new StockPairDailyQuote();

	public StockPairDailyQuote getPairQuote() {
		return pairQuote;
	}

	public void setPairQuote(StockPairDailyQuote pairQuote) {
		this.pairQuote = pairQuote;
	}
	
	public void fetchPairFromCsv(String fileUrl1,
			String fileUrl2) {

		ArrayList<StockHistoricalPrice> result = new ArrayList<>();

		result.add(new StockHistoricalPrice(fileUrl1));
		result.add(new StockHistoricalPrice(fileUrl2));
		StockPairDailyQuote pairDailyQuote = new StockPairDailyQuote();
		pairDailyQuote.StockPairDailyQuoteWithSameDuration(result);
		setPairQuote(pairDailyQuote);
	}
	
	public void fetchPairFromJDBC(StockHistoricalPrice quote1,
			StockHistoricalPrice quote2) {
		ArrayList<StockHistoricalPrice> result = new ArrayList<>();
		result.add(quote1);
		result.add(quote2);
		StockPairDailyQuote pairDailyQuote = new StockPairDailyQuote();
		pairDailyQuote.StockPairDailyQuoteWithSameDuration(result);
		setPairQuote(pairDailyQuote);
	}
	
}
