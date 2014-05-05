package impl.update;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import module.historicalquotes.UpdateHistoricalQuotes;
import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;

public class HistoricalQuoteUpdateMultiThreadVersion {

	private final static String year = String.valueOf(Calendar.getInstance()
			.get(Calendar.YEAR));
	private final static String month = String.valueOf(Calendar.getInstance()
			.get(Calendar.MONTH) + 1);
	private final static String day = String.valueOf(Calendar.getInstance()
			.get(Calendar.DATE));
	private final static String startYear = "1983";
	private final static String startMonth = "1";
	private final static String startDay = "1";
	public static Connection con = null;
	public static Integer count = 0;

	public HistoricalQuoteUpdateMultiThreadVersion() {
		// TODO Auto-generated constructor stub
	}

	public static String getYear() {
		return year;
	}

	public static String getMonth() {
		return month;
	}

	public static String getDay() {
		return day;
	}

	public static String getStartyear() {
		return startYear;
	}

	public static String getStartmonth() {
		return startMonth;
	}

	public static String getStartday() {
		return startDay;
	}

	public static void main(String args[]) {
		Integer splitNumber = 8;
		System.out.println("start updating quotes...");
		CodeListsDao clDao = new CodeListsDao();
		ArrayList<String> codeLists = clDao.getCodeLists();
		count = codeLists.size();
		Integer interval;
		if (count % splitNumber == 0) {
			interval = count / splitNumber;
		} else {
			interval = count / splitNumber + 1;
		}
		ArrayList<ArrayList<String>> codeListGroup = new ArrayList<>();
		for (int i = 0; i < splitNumber; i++) {
			ArrayList<String> newList = new ArrayList<>();
			for (int j = 0; j < interval
					&& (i * interval + j) < codeLists.size(); j++) {
				newList.add(codeLists.get(i * interval + j));
			}
			codeListGroup.add(newList);
		}
		ArrayList<updateThread> threadGroup = new ArrayList<>();
		for (int i = 1; i <= splitNumber; i++) {
			updateThread thread = new updateThread("Thread" + i,
					codeListGroup.get(i - 1));
			System.out.println("Thread" + i + " is created!");
			threadGroup.add(thread);
		}
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
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
	private ArrayList<String> codeList = new ArrayList<>();

	public updateThread() {
		super();
	}

	public updateThread(String str, ArrayList<String> codeList) {
		super(str);
		this.codeList = codeList;
	}

	public void run() {
		UpdateHistoricalQuotes update = new UpdateHistoricalQuotes();
		update.updateCode(codeList, super.getName());
	}

}
