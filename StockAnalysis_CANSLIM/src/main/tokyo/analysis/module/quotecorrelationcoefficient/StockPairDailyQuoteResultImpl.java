package module.quotecorrelationcoefficient;

import impl.quotecorrelationceofficient.QuoteCorrelationCoefficientMultiThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.naming.spi.DirStateFactory.Result;

import commontool.DateOperation;
import mathematics.CorrelationCoefficientCalculator;

public class StockPairDailyQuoteResultImpl {

	private StockPairDailyQuoteResult result;

	public StockPairDailyQuoteResultImpl() {
		this.result = new StockPairDailyQuoteResult();
	}

	public StockPairDailyQuoteResultImpl(StockPairDailyQuote originalData,
			Integer lowLimit, Integer upperLimit, Double refer) {
		this.result = new StockPairDailyQuoteResult();
		this.result = copyBasicInfo(originalData);

		dayDifImpl(originalData, lowLimit, upperLimit, refer);
		// TODO date 2014.2.22
		// weekDifImpl(originalData, lowLimit, upperLimit);
		// monthDifImpl(originalData, lowLimit, upperLimit);
		// yearDifImpl(originalData, lowLimit, upperLimit);
	}

	public void dayDifImpl(StockPairDailyQuote originalData, Integer lowLimit,
			Integer upperLimit, Double refer) {
		Double difWithRefer = 0.0;
		String difWithReferKey = "None";
		ArrayList<Integer> keyList = turnToIntegerList(lowLimit, upperLimit);
		for (Integer key : keyList) {
			key = key * 5;
			ArrayList<StockDailyQuote> newQuoteA = new ArrayList<>();
			ArrayList<StockDailyQuote> newQuoteB = new ArrayList<>();
			if (key < 0
					&& (originalData.getQuoteA().getQuote().size()
							- Math.abs(key) > 2)) {
				newQuoteA = new ArrayList<>(originalData
						.getQuoteA()
						.getQuote()
						.subList(
								0,
								originalData.getQuoteA().getQuote().size()
										- Math.abs(key)));
				newQuoteB = new ArrayList<>(originalData
						.getQuoteB()
						.getQuote()
						.subList(Math.abs(key),
								originalData.getQuoteB().getQuote().size()));
			} else if (originalData.getQuoteB().getQuote().size()
					- Math.abs(key) > 2) {
				newQuoteB = new ArrayList<>(originalData
						.getQuoteB()
						.getQuote()
						.subList(
								0,
								originalData.getQuoteB().getQuote().size()
										- Math.abs(key)));
				newQuoteA = new ArrayList<>(originalData
						.getQuoteA()
						.getQuote()
						.subList(Math.abs(key),
								originalData.getQuoteA().getQuote().size()));
			}
			ArrayList<Double> quoteA = new ArrayList<>();
			ArrayList<Double> quoteB = new ArrayList<>();
			for (StockDailyQuote quote : newQuoteA) {
				quoteA.add(quote.getAdjClose());
			}
			for (StockDailyQuote quote : newQuoteB) {
				quoteB.add(quote.getAdjClose());
			}
			CorrelationCoefficientCalculator ccc = new CorrelationCoefficientCalculator();
			Double result = ccc.getCorrelationCoefficient(quoteA, quoteB);
			if (difWithRefer < Math.abs(result - refer)) {
				difWithRefer = Math.abs(result - refer);
				difWithReferKey = (originalData.getCodeA()+"_"+ 
						originalData.getCodeB() +"_" +String.valueOf(key) + "_same day_" + refer +  "_dif day_" + result + "_day Amount_" + quoteA.size());
			}
			
		}
		QuoteCorrelationCoefficientMultiThread.result.put(difWithReferKey, difWithRefer);
	}

	public void weekDifImpl(StockPairDailyQuote originalData, Integer lowLimit,
			Integer upperLimit) {

		ArrayList<Integer> keyList = turnToIntegerList(lowLimit, upperLimit);
		for (Integer key : keyList) {
			StockPairDailyQuote sdpq = new StockPairDailyQuote();
			sdpq.setWeekDif(key);
			sdpq = weekDifPairQuote(originalData);
			getResult().getWeekDifResult().put(key, sdpq);
		}
	}

	public void monthDifImpl(StockPairDailyQuote originalData,
			Integer lowLimit, Integer upperLimit) {

		ArrayList<Integer> keyList = turnToIntegerList(lowLimit, upperLimit);
		for (Integer key : keyList) {
			StockPairDailyQuote sdpq = new StockPairDailyQuote();
			sdpq.setMonthDif(key);
			sdpq = monthDifPairQuote(originalData);
			getResult().getMonthDifResult().put(key, sdpq);
		}
	}

	public void yearDifImpl(StockPairDailyQuote originalData, Integer lowLimit,
			Integer upperLimit) {

		ArrayList<Integer> keyList = turnToIntegerList(lowLimit, upperLimit);
		for (Integer key : keyList) {
			StockPairDailyQuote sdpq = new StockPairDailyQuote();
			sdpq.setYearDif(key);
			sdpq = yearDifPairQuote(originalData);
			getResult().getYearDifResult().put(key, sdpq);
		}
	}

	public static StockPairDailyQuote dayDifPairQuote(StockPairDailyQuote spdq,
			Integer dayDif) {

		ArrayList<StockDailyQuote> newQuoteA;
		ArrayList<StockDailyQuote> newQuoteB;
		//System.out.println(spdq.getQuoteA().getQuote().size());
		if (dayDif < 0) {
			newQuoteA = new ArrayList<>(spdq
					.getQuoteA()
					.getQuote()
					.subList(
							0,
							spdq.getQuoteA().getQuote().size() - 1
									- Math.abs(dayDif)));
			newQuoteB = new ArrayList<>(spdq
					.getQuoteB()
					.getQuote()
					.subList(Math.abs(dayDif),
							spdq.getQuoteB().getQuote().size() - 1));
		} else {
			newQuoteB = new ArrayList<>(spdq
					.getQuoteB()
					.getQuote()
					.subList(
							0,
							spdq.getQuoteB().getQuote().size() - 1
									- Math.abs(dayDif)));
			newQuoteA = new ArrayList<>(spdq
					.getQuoteA()
					.getQuote()
					.subList(Math.abs(dayDif),
							spdq.getQuoteA().getQuote().size() - 1));
		}
		/*
		 * try { // for (StockDailyQuote quoteA : spdq.getQuoteA().getQuote()) {
		 * Boolean ifFind = false; for (int i = 0, j = 0; i <
		 * spdq.getQuoteA().getQuote().size(); i++) { Date dateA =
		 * sdf.parse(spdq.getQuoteA().getQuote().get(i) .getDate()); if(ifFind
		 * == true) { ifFind = false; } else { j = 0; } System.out.println(j);
		 * for (; j < spdq.getQuoteA().getQuote().size(); j++) { Date dateB =
		 * sdf.parse(spdq.getQuoteA().getQuote().get(j) .getDate());
		 * //System.out.println("loop"); if
		 * (dateA.equals(dO.changeExchangeDay(dateB, dayDif))) {
		 * newQuoteA.add(spdq.getQuoteA().getQuote().get(i));
		 * newQuoteB.add(spdq.getQuoteA().getQuote().get(j));
		 * System.out.print(sdf.format(dateA) + "  ");
		 * System.out.println(sdf.format(dateB)); ifFind = true; break; } } } }
		 * catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		spdq.getQuoteA().setQuote(newQuoteA);
		spdq.getQuoteB().setQuote(newQuoteB);
		//System.out.println("new " + spdq.getQuoteA().getQuote().size());
		//System.out.println("new " + spdq.getQuoteB().getQuote().size());
		/*
		 * StockPairDailyQuote result = new StockPairDailyQuote();
		 * result.setDayDif(dayDif); result = copyBasicInfo(spdq); Date
		 * startDateA = null, startDateB = null, endDateA = null, endDateB =
		 * null; DateOperation dO = new DateOperation(); if (dayDif >= 0) {
		 * startDateA = dO.changeExchangeDay(result.getStart(), -dayDif);
		 * endDateA = dO.changeExchangeDay(result.getEnd(), -dayDif); startDateB
		 * = result.getStart(); endDateB = result.getEnd(); } else { startDateA
		 * = result.getStart(); endDateA = result.getEnd(); startDateB =
		 * dO.changeExchangeDay(result.getStart(), dayDif); endDateB =
		 * dO.changeExchangeDay(result.getEnd(), dayDif); }
		 * 
		 * ArrayList<StockDailyQuote> shpA = new ArrayList<>();
		 * ArrayList<StockDailyQuote> shpB = new ArrayList<>(); shpA =
		 * result.getRawQuoteA().getSubQuote(startDateA, endDateA); shpB =
		 * result.getRawQuoteB().getSubQuote(startDateB, endDateB);
		 * 
		 * result.getQuoteA().setQuote(shpA); result.getQuoteB().setQuote(shpB);
		 * System.out.println(result.getQuoteA().getQuote().size());
		 * System.out.println(result.getQuoteB().getQuote().size());
		 * System.out.println("here4");
		 * 
		 * ArrayList<StockHistoricalPrice> tempHistoricalPrices =
		 * coordinateDate( result.getQuoteA(), result.getQuoteB(), dayDif);
		 * 
		 * result.setQuoteA(tempHistoricalPrices.get(0));
		 * result.setQuoteB(tempHistoricalPrices.get(1));
		 */

		return spdq;

	}

	public static ArrayList<StockHistoricalPrice> coordinateDate(
			StockHistoricalPrice quoteA, StockHistoricalPrice quoteB,
			Integer index) {
		DateOperation dO = new DateOperation();
		Collections.sort(quoteA.getQuote(), new DateComparator());
		/*
		 * for (StockDailyQuote quote : quoteA.getQuote()) {
		 * System.out.println(quote.getDate() + "  " + quote.getAdjClose()); }
		 */
		Collections.sort(quoteB.getQuote(), new DateComparator());
		ArrayList<StockHistoricalPrice> result = new ArrayList<>();
		ArrayList<StockDailyQuote> newQuoteA = new ArrayList<>();
		ArrayList<StockDailyQuote> newQuoteB = new ArrayList<>();
		Date A = new Date();
		Date B = new Date();

		System.out.println(quoteA.getQuote().size());
		System.out.println(quoteB.getQuote().size());
		for (StockDailyQuote sdqA : quoteA.getQuote()) {
			for (StockDailyQuote sdqB : quoteB.getQuote()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
				try {
					A = sdf.parse(sdqA.getDate());
					B = sdf.parse(sdqB.getDate());
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (index >= 0) {
					if (A.equals(dO.changeExchangeDay(B, index))) {
						newQuoteA.add(sdqA);
						newQuoteB.add(sdqB);
						break;
					}
				} else {
					if (A.equals(dO.changeExchangeDay(B, -index))) {
						newQuoteA.add(sdqA);
						newQuoteB.add(sdqB);
						break;
					}
				}
			}
		}
		quoteA.setQuote(newQuoteA);
		quoteB.setQuote(newQuoteB);
		result.add(quoteA);
		result.add(quoteB);

		return result;
	}

	public Date findEarlierDate(Date date1, Date date2) {
		if (date1.before(date2)) {
			return date1;
		} else {
			return date2;
		}
	}

	public Date findLaterDate(Date date1, Date date2) {
		if (date1.before(date2)) {
			return date2;
		} else {
			return date1;
		}
	}

	public static StockPairDailyQuote weekDifPairQuote(StockPairDailyQuote spdq) {
		// TODO
		StockPairDailyQuote result = new StockPairDailyQuote();
		return result;
	}

	public static StockPairDailyQuote monthDifPairQuote(StockPairDailyQuote spdq) {
		// TODO
		StockPairDailyQuote result = new StockPairDailyQuote();
		return result;
	}

	public static StockPairDailyQuote yearDifPairQuote(StockPairDailyQuote spdq) {
		// TODO
		StockPairDailyQuote result = new StockPairDailyQuote();
		return result;
	}

	public static ArrayList<Integer> turnToIntegerList(Integer lowLimit,
			Integer upperLimit) {
		ArrayList<Integer> list = new ArrayList<>();
		Integer element = lowLimit;
		while (element != upperLimit + 1) {
			list.add(element);
			element++;
		}

		return list;
	}

	public static StockPairDailyQuoteResult copyBasicInfo(
			StockPairDailyQuote spdq) {
		StockPairDailyQuoteResult result = new StockPairDailyQuoteResult();
		result.setCodeA(spdq.getCodeA());
		result.setCodeB(spdq.getCodeB());
		result.setNameA(spdq.getNameA());
		result.setNameB(spdq.getNameB());
		result.setStart(spdq.getStart());
		result.setEnd(spdq.getEnd());
		result.setIfHasSameDuration(spdq.getIfHasSameDuration());
		result.setRawQuoteA(spdq.getRawQuoteA());
		result.setRawQuoteB(spdq.getRawQuoteB());
		return result;
	}

	public StockPairDailyQuoteResult getResult() {
		return result;
	}

	public void setResult(StockPairDailyQuoteResult result) {
		this.result = result;
	}

}
