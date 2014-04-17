package impl.update;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import module.shareholdingsplitinfo.HistoricalDataDownloadVolumeSplitRecord;
import datasource.DataSourceUtil;


public class HistoricalDataDownloadVolumeSplitCalibration {

	public HistoricalDataDownloadVolumeSplitCalibration() {

	}

	public static void main(String args[]) {
		//forbidden;
		ArrayList<String> codeList = new ArrayList<>();
		ArrayList<String> wrongList = new ArrayList<>();
		wrongList.add("1661");
		//codeList = new CodeListsDao().getCodeListsFromFinancialStatement();
		Connection con = null;
		String splitTable = "HistoricalStockSplit_test";
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot()
					.getConnection();
			for (String code : codeList) {
				if (Integer.valueOf(code) >= 0) {
					String tableName = code + "_HistoricalQuotes_Tokyo";
					ResultSet rs = con.prepareStatement(
							"SELECT * FROM " + splitTable
									+ " WHERE Local_Code = " + code)
							.executeQuery();
					HistoricalDataDownloadVolumeSplitRecord record = new HistoricalDataDownloadVolumeSplitRecord();
					while (rs.next()) {
						record.setLocal_Code(rs.getString("Local_Code"));
						record.getSplitHistory()
								.put(rs.getString("Split_Date"),
										rs.getFloat("Rate"));
					}
					Set<String> setKey = record.getSplitHistory().keySet();
					try {
						for (String date : setKey) {
							String maxDateSql = "SELECT MAX(Date) FROM "
									+ tableName;
							rs = con.prepareStatement(maxDateSql)
									.executeQuery();
							rs.next();
							System.out.println(rs.getString(1) + "  " + date);
							if (rs.getString(1).compareTo(date) >= 1) {
								String sql = "UPDATE " + tableName + " "
										+ "SET Volume = Volume * "
										+ record.getSplitHistory().get(date)
										+ " WHERE DATE < '" + date + "'";
								con.prepareStatement(sql).execute();
							}
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
						wrongList.add(code);
					}
					System.out.println(code + "   "
							+ (codeList.size() - codeList.indexOf(code))
							+ " to go ");
					for (String wrongcode : wrongList) {
						System.out.print(wrongcode + "  ");
					}
					System.out.println();
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

	}
}
