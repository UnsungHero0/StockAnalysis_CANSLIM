package module.SectorAnalysis;

import java.sql.Connection;

import namespace.DBNameSpace;

public class SectorAnalysisCreateTable {

	private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS "
			+ DBNameSpace.getSectoranalysisDb()
			+ "(Country VARCHAR(50) NOT NULL Default 'Tokyo', "
			+ "Sector_Code INT NOT NULL, " + "Sector_Name VARCHAR(100) , "
			+ "Sector_List_Number INT , " + "Sector_EPS FLOAT , "
			+ "Sector_PER FLOAT , " + "Sector_RSI INT , "
			+ "Sector_Total_Value BIGINT , " + "Sector_Average_Value DOUBLE , "
			+ "PRIMARY KEY (Sector_Code));";

	private static final String DROPTABLE = "DROP TABLE IF EXISTS "
			+ DBNameSpace.getSectoranalysisDb();

	public static void createSectorAnalysisTable(
			SectorAnalysisRecordResult result, Connection con) {

		dropTable(con);
		createTable(con);

		insertIntoDB(result, con);
	}

	public static void dropTable(Connection con) {
		// TODO
	}

	public static void createTable(Connection con) {
		// TODO
	}

	public static void insertIntoDB(SectorAnalysisRecordResult result,
			Connection con) {
		String fields = "(Country, Sector_Code, Sector_Code, Sector_Name, Sector_List_Number, "
				+ "Sector_EPS, Sector_PER, Sector_RSI, Sector_Total_Value, Sector_Average_Value)";
		for(SectorAnalysisRecord record : result.getResult().values()) {
			
			
			String values = "";
			
		}
		
		// TODO
	}

}
