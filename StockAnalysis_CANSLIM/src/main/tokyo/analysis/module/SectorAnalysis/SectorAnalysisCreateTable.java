package module.SectorAnalysis;

import java.sql.Connection;

import commontool.JDBCUtil;

import namespace.DBNameSpace;

public class SectorAnalysisCreateTable {

	private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS "
			+ DBNameSpace.getSectoranalysisDb()
			+ "(Country VARCHAR(50) NOT NULL Default 'Tokyo', "
			+ "Sector_Code VARCHAR(10) NOT NULL, "
			+ "Sector_Name VARCHAR(100) , " + "Sector_List_Number INT , "
			+ "Sector_EPS FLOAT , " + "Secotr_Weight_EPS FLOAT, "
			+ "Sector_PER FLOAT , " + "Sector_RSI INT , "
			+ "Sector_Total_Value BIGINT , " + "PRIMARY KEY (Sector_Code));";

	private static final String DROPTABLE = "DROP TABLE IF EXISTS "
			+ DBNameSpace.getSectoranalysisDb();

	public static void createSectorAnalysisTable(
			SectorAnalysisRecordResult result, Connection con) {

		dropTable(con);
		createTable(con);

		insertIntoDB(result, con);
	}

	public static void dropTable(Connection con) {
		JDBCUtil.excuteQuery(DROPTABLE, con);
	}

	public static void createTable(Connection con) {
		JDBCUtil.excuteQuery(CREATETABLE, con);
	}

	public static void insertIntoDB(SectorAnalysisRecordResult result,
			Connection con) {
		String insertSql = "INSERT INTO TABLE "
				+ DBNameSpace.getSectoranalysisDb();
		String fields = "(Country, Sector_Code, Sector_Code, Sector_Name, Sector_List_Number, "
				+ "Sector_EPS, Secotr_Weight_EPS, Sector_PER, Sector_RSI, Sector_Total_Value)";

		for (SectorAnalysisRecord record : result.getResult().values()) {
			String values = "(";
			values += "Tokyo,'" + record.getSectorName() + "','"
					+ record.getSectorCode() + "','" + record.getSectorName()
					+ "'," + record.getCodeList().size() + ","
					+ record.getSectorEPS() + "," + record.getSectorWeightEPS()
					+ "," + record.getSectorPER() + "," + record.getSectorRSI()
					+ "," + record.getSectorValue() + ")";
			JDBCUtil.excuteQuery(insertSql + " " + fields + " VALUES" + values,
					con);
		}
	}

}
