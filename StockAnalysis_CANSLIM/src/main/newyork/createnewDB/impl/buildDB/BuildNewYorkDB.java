package impl.buildDB;

import impl.listedcompanydownload.DownLoadHistoricalQuotesNewYork;
import impl.listedcompanydownload.DownloadListedCompaniesListNewYork;

public class BuildNewYorkDB {
	
public static void main(String args[]) {
		
		//1. gather the list of stock company
		System.out.println("DownLoad Listed Companies start");
		DownloadListedCompaniesListNewYork.start();
		System.out.println("DownLoadbListed Companies finished");
		
		//2. gather the historical quotes
		System.out.println("Download historical quotes start");
		DownLoadHistoricalQuotesNewYork.start();
		System.out.println("Download historical quotes finished");
		
		//3. financial statements
		
		//4. other indexes
	}

}
