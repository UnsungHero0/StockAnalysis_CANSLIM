package com.analysis.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.analysis.model.StockDailyQuote;

public class DateComparator implements Comparator<StockDailyQuote>{

		@Override
		public int compare(StockDailyQuote o1, StockDailyQuote o2) {
			SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
			Date dateo1 = null;
			Date dateo2 = null;
			try {
			dateo1 = sdf.parse(o1.getDate());
			dateo2 = sdf.parse(o2.getDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return dateo1.compareTo(dateo2);
		}

}
