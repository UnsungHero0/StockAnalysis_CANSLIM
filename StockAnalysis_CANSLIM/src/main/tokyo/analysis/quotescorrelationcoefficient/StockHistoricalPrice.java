package quotescorrelationcoefficient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class StockHistoricalPrice {

	private String nation;
	private String section;
	private String code;
	private String name;
	private ArrayList<StockDailyQuote> quote = new ArrayList<>();

	public StockHistoricalPrice() {

	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<StockDailyQuote> getQuote() {
		return quote;
	}

	public void setQuote(ArrayList<StockDailyQuote> quote) {
		this.quote = quote;
	}

	public StockHistoricalPrice(String url) {
		File file = new File(url);
		String code = file.getName().substring(0, file.getName().indexOf("T"));
		setCode(code);
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			String lineString[] = null;
			String line = null;
			br.readLine();
			while ((line = br.readLine()) != null) {
				lineString = line.split(",");
				if (lineString.length == 7) {
				StockDailyQuote stockquote = new StockDailyQuote();
				stockquote.setDate(lineString[0]);
				stockquote.setOpen(Double.valueOf(lineString[1]));
				stockquote.setHigh(Double.valueOf(lineString[2]));
				stockquote.setLow(Double.valueOf(lineString[3]));
				stockquote.setClose(Double.valueOf(lineString[4]));
				stockquote.setVolume(Double.valueOf(lineString[5]));
				stockquote.setAdjClose(Double.valueOf(lineString[6]));
				getQuote().add(stockquote);
				}
				else
					br.readLine();
			}

			Collections.sort(getQuote(), new Comparator<StockDailyQuote>() {
				public int compare(StockDailyQuote o1, StockDailyQuote o2) {
					Date date1 = null, date2 = null;
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
						date1 = sdf.parse(o1.getDate());
						date2 = sdf.parse(o2.getDate());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (date1.before(date2))
						return 1;
					else
						return -1;
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<StockDailyQuote> getSubQuote(Date startDate, Date endDate) {
		ArrayList<StockDailyQuote> subQuoteList = new ArrayList<>();
		for (StockDailyQuote dailyQuote : getQuote()) {
			if (changeFromStringToDate(dailyQuote.getDate()).after(startDate)
					&& changeFromStringToDate(dailyQuote.getDate()).before(
							endDate)) {
				subQuoteList.add(dailyQuote);
			}
		}
		return subQuoteList;

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

}
