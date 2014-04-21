package quotescorrelationcoefficient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class StockPairDailyQuote {

	private String codeA;
	private String nameA;
	private String codeB;
	private String nameB;
	private Date start;
	private Date end;
	private Integer dayDif = 0;
	private Integer weekDif = 0;
	private Integer monthDif = 0;
	private Integer yearDif = 0;
	private Double correlationCoefficient = 0.0;
	private Boolean ifHasSameDuration = null;
	private StockHistoricalPrice quoteA = new StockHistoricalPrice();
	private StockHistoricalPrice quoteB = new StockHistoricalPrice();
	private StockHistoricalPrice rawQuoteA = new StockHistoricalPrice();
	private StockHistoricalPrice rawQuoteB = new StockHistoricalPrice();
	
	public StockPairDailyQuote() {
	}

	public void StockPairDailyQuoteWithSameDuration(
			ArrayList<StockHistoricalPrice> inputList) {
		
		setRawQuoteA(inputList.get(0));
		setRawQuoteB(inputList.get(1));

		setCodeA(inputList.get(0).getCode());
		setCodeB(inputList.get(1).getCode());
		setNameA(inputList.get(0).getName());
		setNameB(inputList.get(1).getName());
		Date startDateA = null, startDateB = null, endDateA = null, endDateB = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		try {
			endDateA = sdf
					.parse(inputList.get(0).getQuote().get(0).getDate());
			endDateB = sdf
					.parse(inputList.get(1).getQuote().get(0).getDate());
			startDateA = sdf.parse(inputList.get(0).getQuote()
					.get(inputList.get(0).getQuote().size() - 1).getDate());
			startDateB = sdf.parse(inputList.get(1).getQuote()
					.get(inputList.get(1).getQuote().size() - 1).getDate());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		setStartEnd(startDateA, startDateB, endDateA, endDateB);
		StockHistoricalPrice shpA = new StockHistoricalPrice();
		StockHistoricalPrice shpB = new StockHistoricalPrice();
		
		shpA.setQuote((ArrayList<StockDailyQuote>) inputList.get(0).getQuote().clone());
		shpB.setQuote((ArrayList<StockDailyQuote>) inputList.get(1).getQuote().clone());
		
		// find the same exchange day;
		
		setQuoteA(shpA);
		setQuoteB(shpB);

		// record the only same date quote and return result;
		ArrayList<StockHistoricalPrice> result = coordinateDate(getQuoteA(), getQuoteB());
		setQuoteA(result.get(0));
		setQuoteB(result.get(1));
		try {
		setStart(sdf.parse(getQuoteA().getQuote().get(0).getDate()));
		setEnd(sdf.parse(getQuoteA().getQuote().get(getQuoteA().getQuote().size()-1).getDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<StockHistoricalPrice> coordinateDate(
			StockHistoricalPrice quoteA, StockHistoricalPrice quoteB) {
		Collections.sort(quoteA.getQuote(), new DateComparator());
		Collections.sort(quoteB.getQuote(), new DateComparator());
		ArrayList<StockHistoricalPrice> result = new ArrayList<>();
		ArrayList<StockDailyQuote> newQuoteA = new ArrayList<>();
		ArrayList<StockDailyQuote> newQuoteB = new ArrayList<>();

		for (StockDailyQuote sdqA : quoteA.getQuote()) {
			//Boolean ifcontain = false;
			for (StockDailyQuote sdqB : quoteB.getQuote()) {
				if (sdqA.getDate().equals(sdqB.getDate())) {
					newQuoteA.add(sdqA);
					newQuoteB.add(sdqB);
					//ifcontain = true;
					break;
				}
			}
			/*
			if (ifcontain.equals(true))
				newQuoteA.add(sdqA);
				*/
		}

		/*
		for (StockDailyQuote sdqB : quoteB.getQuote()) {
			Boolean ifcontain = false;
			for (StockDailyQuote sdqA : quoteA.getQuote()) {
				if (sdqA.getDate().equals(sdqB.getDate())) {
					ifcontain = true;
					break;
				}
			}
			if (ifcontain.equals(true))
				newQuoteB.add(sdqB);
		}
		*/
		
		System.out.println(newQuoteA.size());
		System.out.println(newQuoteB.size());

		quoteA.setQuote(newQuoteA);
		quoteB.setQuote(newQuoteB);
		result.add(quoteA);
		result.add(quoteB);

		return result;
	}

	public Date changeFromStringToDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
		Date result = null;
		try {
			result = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void setStartEnd(Date startDateA, Date startDateB, Date endDateA,
			Date endDateB) {
		if (endDateA.before(startDateB) || endDateB.before(startDateA)) {
			setIfHasSameDuration(false);
		} else {
			setIfHasSameDuration(true);
			setStart(findLaterDate(startDateA, startDateB));
			setEnd(findEarlierDate(endDateA, endDateB));
		}
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

	public String getCodeA() {
		return codeA;
	}

	public void setCodeA(String codeA) {
		this.codeA = codeA;
	}

	public String getNameA() {
		return nameA;
	}

	public void setNameA(String nameA) {
		this.nameA = nameA;
	}

	public String getCodeB() {
		return codeB;
	}

	public void setCodeB(String codeB) {
		this.codeB = codeB;
	}

	public String getNameB() {
		return nameB;
	}

	public void setNameB(String nameB) {
		this.nameB = nameB;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Double getCorrelationCoefficient() {
		return correlationCoefficient;
	}

	public void setCorrelationCoefficient(
			Double correlationCoefficient) {
		this.correlationCoefficient = correlationCoefficient;
	}

	public Boolean getIfHasSameDuration() {
		return ifHasSameDuration;
	}

	public void setIfHasSameDuration(Boolean ifHasSameDuration) {
		this.ifHasSameDuration = ifHasSameDuration;
	}
	
	public StockHistoricalPrice getQuoteA() {
		return quoteA;
	}

	public void setQuoteA(StockHistoricalPrice quoteA) {
		this.quoteA = quoteA;
	}

	public StockHistoricalPrice getQuoteB() {
		return quoteB;
	}

	public void setQuoteB(StockHistoricalPrice quoteB) {
		this.quoteB = quoteB;
	}

	public Integer getDayDif() {
		return dayDif;
	}

	public void setDayDif(Integer dayDif) {
		this.dayDif = dayDif;
	}

	public StockHistoricalPrice getRawQuoteA() {
		return rawQuoteA;
	}

	public void setRawQuoteA(StockHistoricalPrice rawQuoteA) {
		this.rawQuoteA = rawQuoteA;
	}

	public StockHistoricalPrice getRawQuoteB() {
		return rawQuoteB;
	}

	public void setRawQuoteB(StockHistoricalPrice rawQuoteB) {
		this.rawQuoteB = rawQuoteB;
	}

	public Integer getWeekDif() {
		return weekDif;
	}

	public void setWeekDif(Integer weekDif) {
		this.weekDif = weekDif;
	}

	public Integer getMonthDif() {
		return monthDif;
	}

	public void setMonthDif(Integer monthDif) {
		this.monthDif = monthDif;
	}

	public Integer getYearDif() {
		return yearDif;
	}

	public void setYearDif(Integer yearDif) {
		this.yearDif = yearDif;
	}

}
