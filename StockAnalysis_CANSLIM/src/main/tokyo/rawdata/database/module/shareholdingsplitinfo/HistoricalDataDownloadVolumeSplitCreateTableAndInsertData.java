package module.shareholdingsplitinfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import namespace.DBNameSpace;

public class HistoricalDataDownloadVolumeSplitCreateTableAndInsertData {

	private static final String createTable = "Create Table IF NOT EXISTS "
			+ DBNameSpace.getHistoricalstocksplitDb() + " "
			+ "( Country VARCHAR(50) NOT NULL Default 'Tokyo', "
			+ "Local_Code VARCHAR(20) NOT NULL, " + "Split_Date Date, "
			+ "Rate Float, "
			+ "CONSTRAINT ID PRIMARY KEY (Local_Code, Split_Date));";

	private static final String CHECKIFHAS = "SELECT Count(*) result FROM "
			+ DBNameSpace.getHistoricalstocksplitDb()
			+ " WHERE Local_Code = ? AND Split_Date = ?";

	public HistoricalDataDownloadVolumeSplitCreateTableAndInsertData() {

	}

	public static void createTableAndInsertData(
			ArrayList<HistoricalDataDownloadVolumeSplitRecord> input,
			Connection con) {

		try {
			// System.out.println(createTable);
			con.prepareStatement(createTable).execute();
			for (HistoricalDataDownloadVolumeSplitRecord record : input) {
				insertDataIntoDb(record, con);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void insertDataIntoDb(
			HistoricalDataDownloadVolumeSplitRecord input, Connection con) {
		Set<String> keySet = input.getSplitHistory().keySet();
		String field = "(Local_Code, Split_Date, Rate)";
		try {
			for (String key : keySet) {

				String value = "('" + input.getLocal_Code() + "', '" + key
						+ "', " + input.getSplitHistory().get(key) + ")";
				String insertSql = "INSERT INTO "
						+ DBNameSpace.getHistoricalstocksplitDb() + " " + field
						+ " VALUES " + value;
				// System.out.println(insertSql);

				con.prepareStatement(insertSql).execute();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static Boolean checkIfHasRecord(String code, String date, Connection con) {
		Boolean result = null;
		try {
			PreparedStatement stmt = con.prepareStatement(CHECKIFHAS);
			stmt.setString(1, code);
			stmt.setString(2, date);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			if(rs.getInt("result"))
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
