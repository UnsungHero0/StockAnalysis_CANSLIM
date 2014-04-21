package quotescorrelationcoefficient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;
import mathematics.CorrelationCoefficientCalculator;

/**
 * close price correlation coefficient
 * 
 * @author Daytona
 * 
 */
public class QuoteCorrelationCoefficientJDBC {

	private static Connection con = null;

	public static void main(String args[]) {
		// TODO

		// get all the combination

		System.out.println("start fetching data from JDBC");
		ArrayList<String> codelList = new CodeListsDao().getCodeListsFromFinancialStatement();
		StockHistoricalPrice stockHistoricalPrice = new StockHistoricalPrice();
		ArrayList<StockHistoricalPrice> stockHistoricalPriceList = new ArrayList<>();
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			for (String code : codelList) {
				getStockHistoricalPrice(code, con);
				stockHistoricalPriceList.add(getStockHistoricalPrice(code, con));
				System.out.println(code + "  finished, " + (codelList.size() - codelList.indexOf(code))+ " left!");
				if (codelList.indexOf(code) > 5)
					break;
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
		ArrayList<ArrayList<String>> finalResult = new ArrayList<>();
		Double totalCount = factorial(Double.valueOf(stockHistoricalPriceList.size())) / (factorial(Double.valueOf(stockHistoricalPriceList.size()-2)) * factorial(2.0));
		for (int i = 0; i < stockHistoricalPriceList.size() - 1; i++) {
			StockHistoricalPrice quote1 = stockHistoricalPriceList.get(i);
			for (int j = i + 1; j < stockHistoricalPriceList.size() ; j++) {
				totalCount--;
				StockHistoricalPrice quote2 = stockHistoricalPriceList.get(j);
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
						ArrayList<String> element = new ArrayList<>();

						element.add(pairDailyQuote.getPairQuote().getCodeA());
						element.add(pairDailyQuote.getPairQuote().getCodeB());
						element.add(String.valueOf(result));
						finalResult.add(element);
						System.out.println(totalCount+" is left");
						
						
						Integer upperLimit = 10;
						Integer lowLimit = -10;
						StockPairDailyQuoteResultImpl spdqr = new StockPairDailyQuoteResultImpl();
						spdqr = new StockPairDailyQuoteResultImpl(
								pairDailyQuote.getPairQuote(), lowLimit,
								upperLimit);
								
					}
				}
				catch (IndexOutOfBoundsException e) {
					System.out.println("wrong with " + quote1.getCode()
							+ " and " + quote2.getCode());
				}
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

		Collections.sort(finalResult, new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return Double.valueOf(o1.get(2)).compareTo(Double.valueOf(o2.get(2)));
			}
		});
		System.out.println("The final result is: ");
		for (ArrayList<String> element : finalResult) {
			System.out.println(element.get(0) + " " + element.get(1) + " "
					+ element.get(2));
		}

		// 1 arrange the Adjclose data to the list in the sequence of date

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
			if (!e.getMessage().equals("Value '0000-00-00' can not be represented as java.sql.Date"))
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static Double factorial(Double input) {
		Double result = 1.0;
		for (double i = 1.0 ; i <= input; i ++) {
			result = result * i;
		}
		return result;
	}

}
