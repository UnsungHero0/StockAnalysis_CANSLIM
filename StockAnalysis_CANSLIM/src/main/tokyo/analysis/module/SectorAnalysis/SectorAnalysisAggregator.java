package module.SectorAnalysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import namespace.DBNameSpace;

public class SectorAnalysisAggregator {

	private static final String GETSECTORCODENAME = "SELECT DISTINCT (33_Sector_Code, 33_Sector_Name) FROM "
			+ DBNameSpace.getListedcompaniesTokyoDb();
	private static final String GETCODESECTORCODE = "SELECT Local_Code, 33_Sector_Code FROM "
			+ DBNameSpace.getListedcompaniesTokyoDb()
			+ " WHERE 33_Sector_Code > 0";

	public static SectorAnalysisRecordResult aggregator(Connection con) {
		SectorAnalysisRecordResult result = new SectorAnalysisRecordResult();

		result = setSectorCode(result, con);

		result = setCodeList(result, con);
		
		result = setEPS(result, con);
		
		result = setPER(result,con);
		
		result = setRSI(result, con);
		
		result = setValue(result,con);
		
		return result;

	}

	public static SectorAnalysisRecordResult setSectorCode(
			SectorAnalysisRecordResult result, Connection con) {
		try {
			ResultSet rs = con.prepareStatement(GETSECTORCODENAME)
					.executeQuery();
			while (rs.next()) {
				SectorAnalysisRecord record = new SectorAnalysisRecord();
				record.setSectorCode(rs.getInt("33_Sector_code"));
				record.setSectorName(rs.getString("33_Sector_Name"));
				result.getResult().put(rs.getInt("33_Sector_Code"),
						new SectorAnalysisRecord());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static SectorAnalysisRecordResult setCodeList(
			SectorAnalysisRecordResult result, Connection con) {
		try {
			ResultSet rs = con.prepareStatement(GETCODESECTORCODE)
					.executeQuery();
			while (rs.next()) {
				Integer sector = rs.getInt("33_Sector_code");
				String code = rs.getString("Local_Code");
				result.getResult().get(sector).getCodeList().add(code);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static SectorAnalysisRecordResult setEPS(SectorAnalysisRecordResult result, Connection con) {
		//TODO
		return null;
	}
	
	public static SectorAnalysisRecordResult setPER(SectorAnalysisRecordResult result, Connection con) {
		//TODO
		return null;
	}
	
	public static SectorAnalysisRecordResult setRSI(SectorAnalysisRecordResult result, Connection con) {
		//TODO
		return null;
	}
	
	public static SectorAnalysisRecordResult setValue(SectorAnalysisRecordResult result, Connection con) {
		//TODO
		return null;
	}
}
