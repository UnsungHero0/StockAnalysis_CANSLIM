package impl.quotecorrelationceofficient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;
import mathematics.CorrelationCoefficientCalculator;
import module.quotecorrelationcoefficient.GetPairHistoricalDataFromCsv;
import module.quotecorrelationcoefficient.StockDailyQuote;
import module.quotecorrelationcoefficient.StockHistoricalPrice;
import module.quotecorrelationcoefficient.StockPairDailyQuoteResultImpl;

/**
 * close price correlation coefficient
 * 
 * @author Daytona
 * 
 */
public class QuoteCorrelationCoefficientMultiThread {

	private static Connection con;
	private static Integer threadNumber;
	private static HashMap<String, StockHistoricalPrice> stockHistoricalPriceList = new HashMap<>();
	public static ArrayList<ArrayList<String>> stockPairList = new ArrayList<>();
	private static Double count;
	//private static ArrayList<ArrayList<String>> finalResult = new ArrayList<>();
	public static HashMap<String, Double> result = new HashMap<>();

	public static void main(String args[]) {
		Calendar calendar = Calendar.getInstance();
		Long startTime = calendar.getTimeInMillis();
		threadNumber = 20;
		run();
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;
		System.out.println("running time : " + minute + " minutes " + second + " seconds");
	}

	// get all the combination
	public static void run() {
		System.out.println("start fetching data from JDBC");
		ArrayList<String> codelList = new CodeListsDao()
				.getCodeListsFromFinancialStatement();

		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			for (String code : codelList) {
				if (!code.equals("1661")) {
					stockHistoricalPriceList.put(code,
							getStockHistoricalPrice(code, con));
					System.out.println(code + "  finished, "
							+ (codelList.size() - codelList.indexOf(code))
							+ " left!");
					if (codelList.indexOf(code) > 150)
						break;
				}
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

		System.out.println("finish fetching data from JDBC");

		for (int i = 0; i < stockHistoricalPriceList.keySet().size() - 1; i++) {
			for (int j = i + 1; j < stockHistoricalPriceList.keySet().size(); j++) {
				ArrayList<String> newList = new ArrayList<>();
				newList.add(stockHistoricalPriceList.keySet().toArray()[i]
						.toString());
				newList.add(stockHistoricalPriceList.keySet().toArray()[j]
						.toString());
				stockPairList.add(newList);
			}
		}
		System.out.println(stockPairList.size());
		count = (double) stockPairList.size();

		Integer interval;
		if (count % threadNumber == 0) {
			interval = (int) (count / threadNumber);
		} else {
			interval = (int) (count / threadNumber + 1);
		}
		ArrayList<ArrayList<ArrayList<String>>> codeListGroup = new ArrayList<>();

		for (int i = 0; i < threadNumber; i++) {
			ArrayList<ArrayList<String>> newList = new ArrayList<>();
			for (int j = 0; j < interval
					&& (i * interval + j) < stockPairList.size(); j++) {
				newList.add(stockPairList.get(i * interval + j));
			}
			codeListGroup.add(newList);
		}

		ArrayList<quoteCorrelationCoefficientThread> threadGroup = new ArrayList<>();
		for (int i = 0; i < threadNumber; i++) {
			threadGroup.add(new quoteCorrelationCoefficientThread("Thread"
					+ (i + 1), codeListGroup.get(i)));

		}

		for (int i = 0; i < threadNumber; i++) {
			threadGroup.get(i).start();
			System.out.println(threadGroup.get(i).getName()
					+ " is starting with " + codeListGroup.get(i).size());
		}

		for (int i = 0; i < threadNumber; i++) {
			try {
				threadGroup.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<Entry<String, Double>> resultForSort = new ArrayList<>(
				result.entrySet());

		Collections.sort(resultForSort,
				new Comparator<Entry<String, Double>>() {
					public int compare(Entry<String, Double> o1,
							Entry<String, Double> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				});

		for (Entry<String, Double> entry : resultForSort) {
			String key[] = entry.getKey().split("_");
			if (Double.valueOf(key[8]) > 300
					&& Math.abs(Double.valueOf(key[6])) > 0.85) {
				System.out.println(entry.getKey() + " :  " + entry.getValue());
			}
		}

		System.out.println("over");
	}

	public static void calculatePairStockCoefficientCorrelation(
			ArrayList<ArrayList<String>> codeList) {

		for (ArrayList<String> stockPairCode : codeList) {

			StockHistoricalPrice quote1;
			StockHistoricalPrice quote2;

			quote1 = stockHistoricalPriceList.get(stockPairCode.get(0));
			quote2 = stockHistoricalPriceList.get(stockPairCode.get(1));
			GetPairHistoricalDataFromCsv pairDailyQuote = new GetPairHistoricalDataFromCsv();

			try {
				pairDailyQuote.fetchPairFromJDBC(quote1, quote2);

				if (pairDailyQuote.getPairQuote().getIfHasSameDuration() == true) {

					ArrayList<Double> valueA = getAdjCloseQuote(pairDailyQuote
							.getPairQuote().getQuoteA());
					ArrayList<Double> valueB = getAdjCloseQuote(pairDailyQuote
							.getPairQuote().getQuoteB());

					CorrelationCoefficientCalculator ccc = new CorrelationCoefficientCalculator();
					Double result = ccc.getCorrelationCoefficient(valueA,
							valueB);

					Integer upperLimit = 104;
					Integer lowLimit = -104;
					new StockPairDailyQuoteResultImpl(
							pairDailyQuote.getPairQuote(), lowLimit,
							upperLimit, result);
					synchronized (count) {
						System.out.println(--count + " is left");
					}
				}
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("wrong with " + quote1.getCode() + " and "
						+ quote2.getCode());
			}
		}
	}

	public static ArrayList<Double> getAdjCloseQuote(StockHistoricalPrice shp) {
		ArrayList<Double> result = new ArrayList<>();
		for (int i = 0; i < shp.getQuote().size() - 1; i++) {
			result.add(shp.getQuote().get(i).getAdjClose());
		}
		return result;
	}

	public static StockHistoricalPrice getStockHistoricalPrice(String code,
			Connection con) {
		StockHistoricalPrice result = new StockHistoricalPrice();
		result.setCode(code);
		String sql = "SELECT Date, AdjClose FROM " + code
				+ "_HistoricalQuotes_Tokyo ORDER BY DATE DESC";
		try {
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			while (rs.next()) {
				if (!rs.getString("Date").equals("0000-00-00")) {
					StockDailyQuote quote = new StockDailyQuote();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse(rs.getString("Date"));
					sdf = new SimpleDateFormat("yy/MM/dd");
					quote.setAdjClose(rs.getDouble("AdjClose"));
					quote.setDate(sdf.format(date));
					result.getQuote().add(quote);
				}
			}
		} catch (SQLException e) {
			if (!e.getMessage()
					.equals("Value '0000-00-00' can not be represented as java.sql.Date"))
				e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Double factorial(Double input) {
		Double result = 1.0;
		for (double i = 1.0; i <= input; i++) {
			result = result * i;
		}
		return result;
	}

}

class quoteCorrelationCoefficientThread extends Thread {
	private ArrayList<ArrayList<String>> codeList = new ArrayList<>();

	public quoteCorrelationCoefficientThread() {
		super();
	}

	public quoteCorrelationCoefficientThread(String str,
			ArrayList<ArrayList<String>> codeList) {
		super(str);
		this.codeList = codeList;
	}

	public void run() {
		QuoteCorrelationCoefficientMultiThread
				.calculatePairStockCoefficientCorrelation(codeList);
	}
}
