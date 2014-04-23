package impl.quotecorrelationceofficient;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jdbcdao.CodeListsDao;
import mathematics.CorrelationCoefficientCalculator;
import module.quotecorrelationcoefficient.GetPairHistoricalDataFromCsv;
import module.quotecorrelationcoefficient.StockHistoricalPrice;
import module.quotecorrelationcoefficient.StockPairDailyQuoteResultImpl;


/**
 * close price correlation coefficient
 * 
 * @author Daytona
 * 
 */
public class QuoteCorrelationCoefficient {

	public static void main(String args[]) {
		// TODO

		// get all the combination

		String inputAddress = "result";
		ArrayList<String> addressList = new ArrayList<>();
		File f = new File(inputAddress);
		File list[] = f.listFiles();
		for (File file : list) {
			if (file.getName().contains("csv"))
				addressList.add(file.getAbsolutePath());
		}
		
		ArrayList<String> codelList = new CodeListsDao().getCodeLists();

		ArrayList<ArrayList<String>> finalResult = new ArrayList<>();
		for (int i = 0; i < addressList.size() - 2; i++) {
			String element1 = addressList.get(i);
			for (int j = i + 1; j < addressList.size() - 1; j++) {
				String element2 = addressList.get(j);
				if (!element1.equals(element2)) {
					GetPairHistoricalDataFromCsv pairDailyQuote = new GetPairHistoricalDataFromCsv();
					try {
						pairDailyQuote.fetchPairFromCsv(element1, element2);
						if (pairDailyQuote.getPairQuote()
								.getIfHasSameDuration() == true) {
							ArrayList<Double> valueA = getAdjCloseQuote(pairDailyQuote
									.getPairQuote().getQuoteA());
							ArrayList<Double> valueB = getAdjCloseQuote(pairDailyQuote
									.getPairQuote().getQuoteB());
							CorrelationCoefficientCalculator ccc = new CorrelationCoefficientCalculator();
							Double result = ccc.getCorrelationCoefficient(
									valueA, valueB);
							ArrayList<String> element = new ArrayList<>();
							element.add(pairDailyQuote.getPairQuote()
									.getCodeA());
							element.add(pairDailyQuote.getPairQuote()
									.getCodeB());
							element.add(String.valueOf(result));
							finalResult.add(element);
							System.out.println(element.get(0) + " "
									+ element.get(1) + " " + element.get(2));
							Integer upperLimit = 10;
							Integer lowLimit = -10;
							new StockPairDailyQuoteResultImpl(pairDailyQuote.getPairQuote(), lowLimit, upperLimit);
							}
						} catch (IndexOutOfBoundsException e) {
						System.out.println("wrong with " + element1 + " and "
								+ element2);
					}

				}
			}
		}

		Collections.sort(finalResult, new Comparator<ArrayList<String>>() {
			public int compare(ArrayList<String> o1, ArrayList<String> o2) {
				return o1.get(2).compareTo(o2.get(2));
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

}
