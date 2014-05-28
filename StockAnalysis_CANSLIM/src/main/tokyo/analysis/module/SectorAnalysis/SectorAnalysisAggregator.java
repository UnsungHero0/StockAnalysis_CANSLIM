package module.SectorAnalysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import jdbcdao.CodeListsDao;
import test.urlTest;
import commontool.JDBCUtil;
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

		result = setEPSPER(result, con);

		result = setRSI(result, con);

		result = setValue(result, con);

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

	/**
	 * calculate the average EPS,PER for each sector Sector_EPS,PER is the
	 * average value, Sector_Weight_EPS,PER is the average EPS,PER weighted by
	 * the company total shareholder equity
	 * 
	 * @param result
	 * @param con
	 * @return
	 */
	public static SectorAnalysisRecordResult setEPSPER(
			SectorAnalysisRecordResult result, Connection con) {
		for (SectorAnalysisRecord record : result.getResult().values()) {
			Double ePSSum = 0.0;
			Double assetsXEPSSum = 0.0;
			Double assetsSum = 0.0;
			Double pERSum = 0.0;
			Double assetsXPERSum = 0.0;
			HashMap<String, Double> price = getPrice(con);
			String codeList = turnIntoStringCodeList(record.getCodeList());
			String getEPSAssestSQL1 = "SELECT local_code, Total_Assets, Net_Income / ShareHolding EPS FROM "
					+ "(SELECT code, Net_Income, Total_Assets FROM "
					+ "(SELECT * FROM "
					+ DBNameSpace.getFinancailstatementDb()
					+ " a JOIN "
					+ "(SELECT Local_Code code, max(Fiscal_year) latest_year FROM "
					+ DBNameSpace.getFinancailstatementDb()
					+ " WHERE "
					+ "form = 'consolidate' group by local_code) AS b WHERE "
					+ "local_code = code) AS c WHERE "
					+ "fiscal_year = latest_year AND form = 'consolidate' AND local_code IN ";
			String getEPSAssestSQL2 = ") AS c JOIN "
					+ "ShareHolding_test WHERE code = Local_Code ";
			ResultSet rs = JDBCUtil.excuteQueryWithResult(getEPSAssestSQL1
					+ codeList + getEPSAssestSQL2, con);
			try {
				while (rs.next()) {
					ePSSum += rs.getDouble("EPS");
					pERSum += price.get(rs.getString("Local_Code"))
							/ rs.getDouble("EPS");
					assetsXEPSSum += rs.getDouble("Total_Assets")
							* rs.getDouble("EPS");
					assetsXPERSum += rs.getDouble("Total_Assets")
							* price.get(rs.getString("Local_Code"))
							/ rs.getDouble("EPS");
					assetsSum += rs.getDouble("Total_Assets");
				}
				record.setSectorEPS(ePSSum.floatValue()
						/ (float) record.getCodeList().size());
				record.setSectorPER(pERSum.floatValue()
						/ (float) record.getCodeList().size());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			record.setSectorWeightEPS(assetsXEPSSum.floatValue()
					/ assetsSum.floatValue());
			record.setSectorWeightPER(assetsXPERSum.floatValue()
					/ assetsSum.floatValue());
		}
		return result;
	}

	public static String turnIntoStringCodeList(ArrayList<String> input) {
		String result = "(";
		for (String code : input) {
			result += code + ",";
		}
		result = result.substring(0, result.length() - 1) + ")";
		return result;
	}

	public static HashMap<String, Double> getPrice(Connection con) {
		ArrayList<String> codeList = new CodeListsDao()
				.getCodeListsFromFinancialStatement();
		HashMap<String, Double> price = new HashMap<>();
		String GETLATESTPRICE = "SELECT date,AdjClose FROM ?"
				+ DBNameSpace.getStockhistoricalpriceDb() + " ORDER BY "
				+ "Date DESC LIMIT 1";
		try {
			for (String code : codeList) {
				java.sql.PreparedStatement stmt = con
						.prepareStatement(GETLATESTPRICE);
				stmt.setInt(1, Integer.valueOf(code));
				ResultSet rs = JDBCUtil.excuteQueryWithResult(stmt);
				if (rs.next()) {
					price.put(code, rs.getDouble("AdjClose"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return price;
	}

	public static SectorAnalysisRecordResult setRSI(
			SectorAnalysisRecordResult result, Connection con) {
		HashMap<String, Float> priceIncreasementMap = getPriceIncreasement(con);
		HashMap<String, Float> sectorPriceIncreasementMap = getSectorPriceIncreasement(con);
		HashMap<String, Float> weightSectorPriceIncreasementMap = getweightSectorPriceIncreasementMap(con);
		// TODO
		return null;
	}
	
	public static HashMap<String, Float> getPriceIncreasement(Connection con) {
		
		//TODO
		return null;
	}

	public static SectorAnalysisRecordResult setValue(
			SectorAnalysisRecordResult result, Connection con) {
		// TODO
		return null;
	}
}
