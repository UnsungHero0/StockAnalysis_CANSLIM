package impl.buildDB;

import impl.listedcompanydownload.DownloadHistoricalQuotesSydney;

public class BuildSydneyDB {
	
	public static void main(String args[]) {
		
		//1. gather the list of stock company
		//System.out.println("DownLoad Listed Companies start");
		//DownLoadListedCompaniesListSydney.start();
		//System.out.println("DownLoadbListed Companies finished");
		
		//2. gather the historical quotes
		System.out.println("Download historical quotes start");
		DownloadHistoricalQuotesSydney.start();
		System.out.println("Download historical quotes finished");
		
		//3. financial statements
		
		//4. other indexes
	}

}
