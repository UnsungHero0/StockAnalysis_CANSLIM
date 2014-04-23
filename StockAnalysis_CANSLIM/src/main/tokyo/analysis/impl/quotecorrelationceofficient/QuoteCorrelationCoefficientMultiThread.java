package impl.quotecorrelationceofficient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
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
	private static ArrayList<ArrayList<String>> finalResult = new ArrayList<>();
	public static HashMap<String, Double> result = new HashMap<>();

	public static void main(String args[]) {
		Calendar calendar = Calendar.getInstance();
		Long startTime = calendar.getTimeInMillis();
		threadNumber = 20;
		run();
		System.out.println("10 -> 62s  " + (Calendar.getInstance().getTimeInMillis()-startTime) / (1000));
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
				if (codelList.indexOf(code) > 99999)
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
		/*
		 * totalCount =
		 * factorial(Double.valueOf(stockHistoricalPriceList.size())) /
		 * (factorial(Double .valueOf(stockHistoricalPriceList.size() - 2)) *
		 * factorial(2.0));
		 * 
		 * System.out.println(totalCount);
		 */

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
					+ (i + 1),codeListGroup.get(i)));
			
		}

		for (int i = 0; i < threadNumber; i++) {
			threadGroup.get(i).start();
			System.out.println(threadGroup.get(i).getName() + " is starting with " + codeListGroup.get(i).size());
		}

		for (int i = 0; i < threadNumber; i++) {
			try {
				threadGroup.get(i).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/*
		Collections.sort(finalResult, new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return Double.valueOf(o1.get(2)).compareTo(
						Double.valueOf(o2.get(2)));
			}
		});

		System.out.println("The final result is: ");
		for (ArrayList<String> element : finalResult) {
			System.out.println(element.get(0) + " " + element.get(1) + " "
					+ element.get(2));
		}
		*/
		ArrayList<Entry<String, Double>> resultForSort = new ArrayList<>(result.entrySet());
		
		Collections.sort(resultForSort, new Comparator<Entry<String, Double>>() {
			public int compare(Entry<String, Double> o1,Entry<String, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		
		for (Entry<String, Double> entry : resultForSort) {
			String key[] = entry.getKey().split("_");
			if(Double.valueOf(key[8]) > 300 && Math.abs(Double.valueOf(key[6])) > 0.85) {
			System.out.println(entry.getKey() + " :  " + entry.getValue());
			}
		}

		System.out.println("over");
	}

	public static void calculatePairStockCoefficientCorrelation(
			ArrayList<ArrayList<String>> codeList) {
		/*
		 * Boolean ifHasNext = false; ArrayList<String> stockPairCode = new
		 * ArrayList<>(); synchronized (stockPairList) { if
		 * (stockPairList.size() != 0) { ifHasNext = true; stockPairCode =
		 * stockPairList.get(0); stockPairList.remove(0); } }
		 * 
		 * while (ifHasNext == true) {
		 */

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
							
					/*
					ArrayList<String> element = new ArrayList<>();

					element.add(pairDailyQuote.getPairQuote().getCodeA());
					element.add(pairDailyQuote.getPairQuote().getCodeB());
					element.add(String.valueOf(result));
					
					synchronized (finalResult) {
						finalResult.add(element);
					}
					*/

					Integer upperLimit = 104;
					Integer lowLimit = -104;
					//StockPairDailyQuoteResultImpl spdqr = new StockPairDailyQuoteResultImpl();
					new StockPairDailyQuoteResultImpl(
							pairDailyQuote.getPairQuote(), lowLimit, upperLimit, result);
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

		/*
		 * ArrayList<ArrayList<String>> finalResult = new ArrayList<>(); for
		 * (int i = 0; i < addressList.size() - 2; i++) { String element1 =
		 * addressList.get(i); for (int j = i + 1; j < addressList.size() - 1;
		 * j++) { String element2 = addressList.get(j); if
		 * (!element1.equals(element2)) { GetPairHistoricalDataFromCsv
		 * pairDailyQuote = new GetPairHistoricalDataFromCsv();
		 * 
		 * try {
		 * 
		 * pairDailyQuote.fetchPairFromCsv(element1, element2);
		 * 
		 * if (pairDailyQuote.getPairQuote() .getIfHasSameDuration() == true) {
		 * ArrayList<Double> valueA = getAdjCloseQuote(pairDailyQuote
		 * .getPairQuote().getQuoteA()); ArrayList<Double> valueB =
		 * getAdjCloseQuote(pairDailyQuote .getPairQuote().getQuoteB());
		 * CorrelationCoefficientCalculator ccc = new
		 * CorrelationCoefficientCalculator(); Double result =
		 * ccc.getCorrelationCoefficient( valueA, valueB); ArrayList<String>
		 * element = new ArrayList<>();
		 * element.add(pairDailyQuote.getPairQuote() .getCodeA());
		 * element.add(pairDailyQuote.getPairQuote() .getCodeB());
		 * element.add(String.valueOf(result)); finalResult.add(element);
		 * System.out.println(element.get(0) + " " + element.get(1) + " " +
		 * element.get(2)); Integer upperLimit = 10; Integer lowLimit = -10;
		 * StockPairDailyQuoteResultImpl spdqr = new
		 * StockPairDailyQuoteResultImpl(); spdqr = new
		 * StockPairDailyQuoteResultImpl( pairDailyQuote.getPairQuote(),
		 * lowLimit, upperLimit); } } catch (IndexOutOfBoundsException e) {
		 * System.out.println("wrong with " + element1 + " and " + element2); }
		 * 
		 * } } }
		 */
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

	public quoteCorrelationCoefficientThread(String str, ArrayList<ArrayList<String>> codeList) {
		super(str);
		this.codeList = codeList;
	}

	public void run() {
		QuoteCorrelationCoefficientMultiThread
				.calculatePairStockCoefficientCorrelation(codeList);
	}
}
