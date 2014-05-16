package module.ListOfTSEListed;

import java.util.HashMap;

public class ListOfTSEListedFileURL {

	private static final String FIRST_SECTION_DOMESTIC = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/b7gje6000001gp95.xls";
	private static final String FIRST_SECTION_FOREIGN = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/first-f-e.xls";
	private static final String SECOND_SECTION = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/second-d-e.xls";
	private static final String MOTHERS_DOMESTIC = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/mothers-d-e.xls";
	private static final String MOTHERS_FOREIGN = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/mothers-f-e.xls";
	private static final String REIT = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/reit-e.xls";
	private static final String ETF_ETN = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/etp-e.xls";
	private static final String PRO_Market = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/b7gje6000000v9vz.xls";
	private static final String JASDAQ_GROWTH = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/b7gje60000013hjc.xls";
	private static final String JASDAQ_STANDARD_DOMESTIC = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/b7gje6000001m64o.xls";
	private static final String JASDAQ_STANDARD_FOREIGN = "http://www.tse.or.jp/english/market/data/listed_companies/b7gje6000000rw32-att/b7gje6000001m64u.xls";
	
	public static String getFirstSectionDomestic() {
		return FIRST_SECTION_DOMESTIC;
	}
	public static String getFirstSectionForeign() {
		return FIRST_SECTION_FOREIGN;
	}
	public static String getSecondSection() {
		return SECOND_SECTION;
	}
	public static String getMothersDomestic() {
		return MOTHERS_DOMESTIC;
	}
	public static String getMothersForeign() {
		return MOTHERS_FOREIGN;
	}
	public static String getReit() {
		return REIT;
	}
	public static String getEtfEtn() {
		return ETF_ETN;
	}
	public static String getProMarket() {
		return PRO_Market;
	}
	public static String getJasdaqGrowth() {
		return JASDAQ_GROWTH;
	}
	public static String getJasdaqStandardDomestic() {
		return JASDAQ_STANDARD_DOMESTIC;
	}
	public static String getJasdaqStandardForeign() {
		return JASDAQ_STANDARD_FOREIGN;
	}
	
	public static HashMap<String, String> getAllListURL(){
		HashMap<String, String> result = new HashMap<>();
		result.put("SecondSection", SECOND_SECTION);
		result.put("REITVentureFundsCountryFunds", REIT);
		result.put("PROMarket", PRO_Market);
		result.put("Mothers(Foreign)", MOTHERS_FOREIGN);
		result.put("Mothers(Domestic)", MOTHERS_DOMESTIC);
		result.put("JASDAQ(StandardForeign)", JASDAQ_STANDARD_FOREIGN);
		result.put("JASDAQ(StandardDomestic)", JASDAQ_STANDARD_DOMESTIC);
		result.put("JASDAQ(Growth)", JASDAQ_GROWTH);
		result.put("ETFETN", ETF_ETN);
		result.put("FirstSection(Domestic)", FIRST_SECTION_DOMESTIC);
		result.put("FirstSection(Foreign)", FIRST_SECTION_FOREIGN);
		return result;
	}

}
