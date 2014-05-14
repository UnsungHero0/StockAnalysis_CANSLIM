package module.shareholding;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import namespace.DBNameSpace;
import commontool.JDBCUtil;
import dao.UrlDao;
import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;

public class ShareHoldingDownloadMultiThread {

	private static final String createTableSql = "Create Table IF NOT EXISTS "
			+ DBNameSpace.getShareholdingDb() + " "
			+ "( Country VARCHAR(50) NOT NULL Default 'Tokyo', "
			+ "Local_Code VARCHAR(20) NOT NULL, " + "Date Date, "
			+ "ShareHolding Double, " + "PRIMARY KEY (Local_Code));";

	public static Integer threadNumber = 8;
	public static ArrayList<String> codeList = new ArrayList<>();
	public static Integer totalCount = 0;
	public static Connection con = null;

	public ShareHoldingDownloadMultiThread() {

	}

	public static void run(Integer threadNumber) {
		
		ShareHoldingDownloadMultiThread.threadNumber = threadNumber;

		codeList = new CodeListsDao().getCodeListsFromFinancialStatement();
		totalCount = codeList.size();
		ArrayList<ShareHoldingDownloadThread> threadGroup = new ArrayList<>();
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			dropTable(con);
			createTable(con);
			for (int i = 0; i < threadNumber; i++) {
				threadGroup.add(new ShareHoldingDownloadThread("Thread"
						+ (i + 1)));
				threadGroup.get(i).start();
				System.out.println("Thread" + (i + 1) + " is starting");
			}
			for (ShareHoldingDownloadThread thread : threadGroup) {
				try {
					thread.join();
					System.out.println(thread.getName() + " is finished!");
				} catch (InterruptedException e) {
					e.printStackTrace();
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

		System.out.println("ShareHolding update is finished!");
	}

	public static void dropTable(Connection con) {
		JDBCUtil.dropTable(DBNameSpace.getShareholdingDb(), con);
	}

	public static void createTable(Connection con) {
		JDBCUtil.excuteQuery(createTableSql, con);
	}

	public static void download(Connection con, String threadName) {

		String code = null;
		Boolean ifHashNext = false;
		synchronized (codeList) {
			code = codeList.get(0);
			codeList.remove(0);
			ifHashNext = true;
		}
		while (ifHashNext == true) {
			if (Integer.valueOf(code) >= 0) {
				updateShareHoldingWithCode(code, con);
			}
			synchronized (totalCount) {
				System.out.println(threadName + ": " + code + " is downloaded, "
						+ --totalCount + " is left");
			}
			synchronized (codeList) {
				if (codeList.size() != 0) {
					code = codeList.get(0);
					codeList.remove(0);
				} else {
					ifHashNext = false;
				}
			}
		}
	}

	public static void updateShareHoldingWithCode(String code, Connection con) {
		Double shareHolding = getShareHoldingFromUrl(code);
		String date = getTodayDate();
		String value = combineValue(code, date, shareHolding);
		String field = "(Local_Code, Date, ShareHolding)";
		insertIntoDB(field, value, con);
	}
	
	public static String getTodayDate(){
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
		if(month.length()==1) {
			month = "0" + month;
		}
		String day = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
		if(day.length()==1) {
			day = "0" + day;
		}
		return year+month+day;
	}

	public static Double getShareHoldingFromUrl(String code) {
		String urlString = "http://stocks.finance.yahoo.co.jp/stocks/detail/?code="
				+ code;
		ArrayList<String> result = UrlDao.getUrlBuffer(urlString);
		Integer colum = 0;
		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).contains("発行済株式数")
					&& result.get(i).contains("href")) {
				colum = i - 1;
			}
		}
		String volume = result.get(colum).split("<strong>")[1].substring(0,
				result.get(colum).split("<strong>")[1].indexOf("</strong>"));
		String volumeResult = "";
		for (String element : volume.split(",")) {
			volumeResult += element;
		}
		return Double.valueOf(volumeResult);
	}

	public static String combineValue(String code, String date,
			Double shareHolding) {
		return "(" + code + ", " + date + ", " + shareHolding + ")";
	}

	public static void insertIntoDB(String field, String value, Connection con) {
		synchronized (con) {
			JDBCUtil.insertData(DBNameSpace.getShareholdingDb(), field, value, con);
		}
	}
}

class ShareHoldingDownloadThread extends Thread {

	private Connection con = ShareHoldingDownloadMultiThread.con;

	public ShareHoldingDownloadThread() {
		super();
	}

	public ShareHoldingDownloadThread(String str) {
		super(str);
	}

	public void run() {
		ShareHoldingDownloadMultiThread.download(con, super.getName());
	}
}
