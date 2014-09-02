package impl.buildDB;

import impl.listedcompanydownload.DownLoadListedCompaniesListSydney;
import dao.downLoadJapaneseToEnglish;

public class BuildSydneyDB {
	
	public static void main(String args[]) {
		
		//1. gather the list of stock company
		System.out.println("DownLoadListedCompanies start");
		DownLoadListedCompaniesListSydney.start();
		System.out.println("DownLoadListedCompanies over");
		
		//2. gather the historical quotes 
		
		
		//3. financial statements
		
		//4. other indexes
	}

}
