package module.shareholding;

/**
 * update the shareholding DB from http://stocks.finance.yahoo.co.jp/stocks/detail/?code=XXX
 * @author Daytona
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import namespace.DBNameSpace;
import commontool.JDBCUtil;
import dao.UrlDao;
import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;

public class ShareHoldingUpdateMultiThread {
	
	public static Integer threadNumber = 8;
	public static ArrayList<String> codeList = new ArrayList<>();
	public static Integer totalCount = 0;
	public static Connection con = null;
	public static HashMap<String, Double> presentResult = new HashMap<>();

	public ShareHoldingUpdateMultiThread() {
	}

	public static void run(Integer threadNumber) {

		ShareHoldingUpdateMultiThread.threadNumber = threadNumber;
		codeList = new CodeListsDao().getCodeListsFromFinancialStatement();
		totalCount = codeList.size();
		ArrayList<ShareHoldingUpdateThread> threadGroup = new ArrayList<>();
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			getPresentResult(con);
			for (int i = 0; i < threadNumber; i++) {
				threadGroup
						.add(new ShareHoldingUpdateThread("Thread" + (i + 1)));
				threadGroup.get(i).start();
				System.out.println("Thread" + (i + 1) + " is starting");
			}
			for (ShareHoldingUpdateThread thread : threadGroup) {
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

	public static void getPresentResult(Connection con) {
		try {
			ResultSet rs = con.prepareStatement(
					"SELECT * FROM " + DBNameSpace.getShareholdingDb())
					.executeQuery();
			while (rs.next()) {
				String code = rs.getString("Local_Code");
				Double shareHolding = rs.getDouble("ShareHolding");
				presentResult.put(code, shareHolding);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Connection con, String threadName) {

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
				System.out.println(threadName + ": " + code
						+ " is updated, " + --totalCount + " is left");
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
		Boolean ifNew = null;
		synchronized (presentResult) {
			ifNew = presentResult.containsKey(code) ? false : true;
		}
		if (ifNew) {
			String date = getTodayDate();
			String value = combineValue(code, date, shareHolding);
			String field = "(Local_Code, Date, ShareHolding)";
			insertIntoDB(field, value, con);
		} else {
			if (ifHasNewValue(code, shareHolding)) {
				String date = getTodayDate();
				updateDB(code, shareHolding, date, con);
				System.out.println(code + " has new value!");
			}
		}
	}

	public static Boolean ifHasNewValue(String code, Double shareHolding) {
		synchronized (presentResult) {
			return String.valueOf(presentResult.get(code)).equals(
					String.valueOf(shareHolding)) ? false : true;
		}
	}

	public static String getTodayDate() {
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String month = String.valueOf(Calendar.getInstance()
				.get(Calendar.MONTH) + 1);
		if (month.length() == 1) {
			month = "0" + month;
		}
		String day = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
		if (day.length() == 1) {
			day = "0" + day;
		}
		return year + month + day;
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
			JDBCUtil.insertData(DBNameSpace.getShareholdingDb(), field, value,
					con);
		}
	}

	public static void updateDB(String code, Double value, String date,
			Connection con) {
		String sql = "update " + DBNameSpace.getShareholdingDb() + " SET "
				+ "ShareHolding = " + value + ", Date = " + date + " "
				+ "WHERE Local_Code = " + code;
		try {
			synchronized (con) {
				con.prepareStatement(sql).execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

class ShareHoldingUpdateThread extends Thread {

	private Connection con = ShareHoldingUpdateMultiThread.con;

	public ShareHoldingUpdateThread() {
		super();
	}

	public ShareHoldingUpdateThread(String str) {
		super(str);
	}

	public void run() {
		ShareHoldingUpdateMultiThread.update(con, super.getName());
	}
}
