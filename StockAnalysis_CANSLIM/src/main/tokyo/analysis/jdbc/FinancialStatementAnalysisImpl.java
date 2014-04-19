package jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.sql.DataSource;

import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;
import jdbcdao.SectionDao;

public class FinancialStatementAnalysisImpl {

	public FinancialStatementAnalysisImpl() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		ThreadEvenNumberOutput threadEven = new ThreadEvenNumberOutput();
		ThreadOddNumberOutput threadOdd = new ThreadOddNumberOutput();
		threadEven.start();
		threadOdd.start();
	}

	public static ArrayList<Entry<String, FinancialStatementAnalysisRecord>> evenNumberOutput() {
		ArrayList<String> codeList = new CodeListsDao()
				.getCodeListsFromFinancialStatement();
		HashMap<String, FinancialStatementAnalysisRecord> resultMap = new HashMap<>();
		Connection con = null;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			new SectionDao(con);
			HashMap<String, String> section = SectionDao.getSectionInfo();
			for (String code : codeList) {
				FinancialStatementAnalysisRecord record = new FinancialStatementAnalysisRecord();
				record.setLocal_Code(code);
				record.setSector_Name(section.get(code));
				resultMap.put(code, record);
			}
			String form = "consolidate";
			resultMap = new FinancialStatementAnalysisYearlyGrowthRate()
					.getGrowthRate(resultMap, "eps", form, con);
			resultMap = new FinancialStatementAnalysisYearlyGrowthRate()
					.getGrowthRate(resultMap, "bps", form, con);
			resultMap = new FinancialStatementAnalysisYearlyGrowthRate()
					.getGrowthRate(resultMap, "sales", form, con);
			resultMap = new FinancialStatementAnalysisRSI().calculateRSI(
					resultMap, con);
			resultMap = new FinancialStatementAnalysisQuarterGrowthRate()
					.getQuarterGrowthRate(resultMap, "Net_Income", con);
			resultMap = FinancialStatementAnalysisVolume.getVolumeInfo(
					resultMap, con);

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
		print(filter(sort(new ArrayList<>(resultMap.entrySet()))));
		return null;
	}

	public static DataSource getDataSource() {
		return DataSourceUtil.getTokyoDataSourceRoot();
	}

	public static ArrayList<Entry<String, FinancialStatementAnalysisRecord>> sort(
			ArrayList<Entry<String, FinancialStatementAnalysisRecord>> resultEntrySet) {
		Collections
				.sort(resultEntrySet,
						new Comparator<Entry<String, FinancialStatementAnalysisRecord>>() {
							public int compare(
									Entry<String, FinancialStatementAnalysisRecord> o1,
									Entry<String, FinancialStatementAnalysisRecord> o2) {
								return o2
										.getValue()
										.getTodayToFiftyWeeksAverage()
										.compareTo(
												o1.getValue()
														.getTodayToFiftyWeeksAverage());
							}
						});
		return resultEntrySet;
	}

	public static ArrayList<Entry<String, FinancialStatementAnalysisRecord>> filter(
			ArrayList<Entry<String, FinancialStatementAnalysisRecord>> resultEntrySet) {
		ArrayList<Entry<String, FinancialStatementAnalysisRecord>> result = new ArrayList<>();
		for (Entry<String, FinancialStatementAnalysisRecord> record : resultEntrySet) {
			if (record.getValue().getePSAverageGrowthRate() >= 0.25
					&& record.getValue().getRSIInAllStock() >= 85
					&& record.getValue().getTodayToFiftyWeeksAverage() >= 0.7) {
				boolean ifqualified = true;
				for (Float value : record.getValue().getePSGrowthRateArray()) {
					if (value < 0.20f) {
						ifqualified = false;
						break;
					}
				}

				if (ifqualified == true) {
					result.add(record);
				}

			}
		}
		return result;
	}

	public static void print(
			ArrayList<Entry<String, FinancialStatementAnalysisRecord>> resultEntrySet) {
		for (Entry<String, FinancialStatementAnalysisRecord> record : resultEntrySet) {
			System.out
					.print(record.getValue().getLocal_Code()
							+ "  "
							+ record.getValue().getSector_Name()
							+ "  EPS_AVE "
							+ (int) (record.getValue()
									.getePSAverageGrowthRate() * 100)
							+ "%  YearSALES_AVE "
							+ (int) (record.getValue()
									.getSalesAverageGrowthRate() * 100)
							+ "%  BPS_AVE "
							+ (int) (record.getValue()
									.getbPSAverageGrowthRate() * 100)

							+ "%  RSI "
							+ record.getValue().getRSIInAllStock().intValue()
							+ "  QuarterSLAES_AVE "
							+ (int) (record.getValue()
									.getNet_IncomeQuarterAverageGrowthRate() * 100)
							+ "%  "
							+ "  EPS / BPS "
							+ (int) (record.getValue()
									.getePSAverageGrowthRate() / record
									.getValue().getbPSAverageGrowthRate())
							+ " Volume "
							+ (int) (record.getValue()
									.getTodayToFiftyWeeksAverage() * 100) + "%"
							+ "  "
							+ record.getValue().getFiftyWeekAverageVolume()
							+ "\n");
		}
	}
}

class ThreadEvenNumberOutput extends Thread {
	public ThreadEvenNumberOutput() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void run() {
		FinancialStatementAnalysisImpl.evenNumberOutput();
	}
}

class ThreadOddNumberOutput extends Thread {
	public ThreadOddNumberOutput() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void run() {
		System.out.println("I am odd");
		try {
			Thread.sleep(7000);// 主线程挂起7秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("\nI am odd");
		//FinancialStatementAnalysisImpl.evenNumberOutput();
	}
}