package quotescorrelationcoefficient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import commontool.DateOperation;

import mathematics.CorrelationCoefficientCalculator;

public class StockPairDailyQuoteResultImpl {

	private StockPairDailyQuoteResult result;

	public StockPairDailyQuoteResultImpl() {
		this.result = new StockPairDailyQuoteResult();
	}

	public StockPairDailyQuoteResultImpl(StockPairDailyQuote originalData,
			Integer lowLimit, Integer upperLimit) {
		this.result = new StockPairDailyQuoteResult();
		this.result = copyBasicInfo(originalData);
		dayDifImpl(originalData, lowLimit, upperLimit);
		//TODO date 2014.2.22
		//weekDifImpl(originalData, lowLimit, upperLimit);
		//monthDifImpl(originalData, lowLimit, upperLimit);
		//yearDifImpl(originalData, lowLimit, upperLimit);
	}

	public void dayDifImpl(StockPairDailyQuote originalData, Integer lowLimit,
			Integer upperLimit) {

		ArrayList<Integer> keyList = turnToIntegerList(lowLimit, upperLimit);
		for (Integer key : keyList) {
			StockPairDailyQuote sdpq = new StockPairDailyQuote();
			sdpq.setDayDif(key);
			sdpq = dayDifPairQuote(originalData, key);
			ArrayList<Double> quoteA = new ArrayList<>();
			ArrayList<Double> quoteB = new ArrayList<>();
			for (StockDailyQuote quote: sdpq.getQuoteA().getQuote()) {
				quoteA.add(quote.getAdjClose());
			}
			for (StockDailyQuote quote: sdpq.getQuoteB().getQuote()) {
				quoteB.add(quote.getAdjClose());
			}
			CorrelationCoefficientCalculator ccc = new CorrelationCoefficientCalculator();
			sdpq.setCorrelationCoefficient(ccc.getCorrelationCoefficient(quoteA, quoteB));
			getResult().getDayDifResult().put(key, sdpq);
			System.out.println(key +  " " + sdpq.getCorrelationCoefficient());
			//TODO 
		}

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

		StockPairDailyQuote result = new StockPairDailyQuote();
		result.setDayDif(dayDif);
		result = copyBasicInfo(spdq);
		Date startDateA = null, startDateB = null, endDateA = null, endDateB = null;
		DateOperation dO = new DateOperation();
		if (dayDif >= 0) {
			startDateA = dO.changeExchangeDay(result.getStart(), -dayDif);
			endDateA = dO.changeExchangeDay(result.getEnd(), -dayDif);
			startDateB = result.getStart();
			endDateB = result.getEnd();
		} else {
			startDateA = result.getStart();
			endDateA = result.getEnd();
			startDateB = dO.changeExchangeDay(result.getStart(), dayDif);
			endDateB = dO.changeExchangeDay(result.getEnd(), dayDif);
			}
		
		ArrayList<StockDailyQuote> shpA = new ArrayList<>();
		ArrayList<StockDailyQuote> shpB = new ArrayList<>();
		shpA = result.getRawQuoteA().getSubQuote(startDateA, endDateA);
		shpB = result.getRawQuoteB().getSubQuote(startDateB, endDateB);

		result.getQuoteA().setQuote(shpA);
		result.getQuoteB().setQuote(shpB);

		ArrayList<StockHistoricalPrice> tempHistoricalPrices = coordinateDate(
				result.getQuoteA(), result.getQuoteB(), dayDif);

		result.setQuoteA(tempHistoricalPrices.get(0));
		result.setQuoteB(tempHistoricalPrices.get(1));

		return result;
	}

	public static ArrayList<StockHistoricalPrice> coordinateDate(
			StockHistoricalPrice quoteA, StockHistoricalPrice quoteB,
			Integer index) {
		DateOperation dO = new DateOperation();
		Collections.sort(quoteA.getQuote(), new DateComparator());
		Collections.sort(quoteB.getQuote(), new DateComparator());
		ArrayList<StockHistoricalPrice> result = new ArrayList<>();
		ArrayList<StockDailyQuote> newQuoteA = new ArrayList<>();
		ArrayList<StockDailyQuote> newQuoteB = new ArrayList<>();
		Date A = new Date();
		Date B = new Date();

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

	public static StockPairDailyQuoteResult copyBasicInfo(StockPairDailyQuote spdq) {
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
