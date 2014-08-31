package impl.download;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.sql.DataSource;

import jdbcdao.CodeListsDao;
import module.historicalquotes.CreateQuotesTableFromUrl;
import datasource.DataSourceUtil;

public class HistoricalQuoteDownload {

	public static DataSource datasource = DataSourceUtil.DINGUNSW();
	public static ArrayList<String> codeLists = new ArrayList<>();
	public static Connection con = null;
	public static Integer count = 0;
	public static Integer threadNumber = 1;

	public static void main(String args[]) {
		start();
	}

	public static void start() {

		Long startTime = Calendar.getInstance().getTimeInMillis();
		run(HistoricalQuoteDownload.threadNumber);
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long) (1000 * 60));
		Integer second = (int) ((endTime - startTime) / (long) (1000)) % 60;
		System.out.println("running time : " + minute + " minutes " + second
				+ " seconds");
	}

	public static void run(Integer splitNumber) {
		System.out.println("start downloading quotes...");
		codeLists = new CodeListsDao().getCodeLists(datasource);
		//codeLists.add("1301");
		count = codeLists.size();
		
		System.out.println(count);
		ArrayList<updateThread> threadGroup = new ArrayList<>();
		for (int i = 1; i <= splitNumber; i++) {
			updateThread thread = new updateThread("Thread" + i);
			System.out.println("Thread" + i + " is created!");
			threadGroup.add(thread);
		}
		try {
			con = datasource.getConnection();
			for (updateThread thread : threadGroup) {
				thread.start();
				System.out.println(thread.getName() + " is starting");
			}
			for (updateThread thread : threadGroup) {
				thread.join();
				System.out.println(thread.getName() + " is finished!");
			}
			System.out.println("finished");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
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
	}

}

class updateThread extends Thread {
	public updateThread() {
		super();
	}

	public updateThread(String str) {
		super(str);
	}

	public void run() {
		String code = "";
		Boolean ifHasNext = false;
		synchronized (HistoricalQuoteDownload.codeLists) {
			if (HistoricalQuoteDownload.codeLists.size() > 0) {
				code = HistoricalQuoteDownload.codeLists.get(0);
				HistoricalQuoteDownload.codeLists.remove(0);
				ifHasNext = true;
			}
		}
		while (ifHasNext == true) {
			CreateQuotesTableFromUrl.createNewQuotesTable(code, HistoricalQuoteDownload.con);
			synchronized (HistoricalQuoteDownload.count) {
				System.out.println(super.getName() + ": HistoricalQuotes : " + code + " is downloaded, "
						+ --HistoricalQuoteDownload.count
						+ " to go!");
			}
			synchronized (HistoricalQuoteDownload.codeLists) {
				if (HistoricalQuoteDownload.codeLists.size() > 0) {
					code = HistoricalQuoteDownload.codeLists
							.get(0);
					HistoricalQuoteDownload.codeLists.remove(0);
				} else {
					ifHasNext = false;
				}
			}
		}
	}

}
