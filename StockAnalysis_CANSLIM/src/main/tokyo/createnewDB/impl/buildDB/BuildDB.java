package impl.buildDB;

import impl.download.HistoricalQuoteDownload;

//create database on a new warehouse


public class BuildDB {
	
	public static void main(String args[]) {
		
		// TODO Auto-generated constructor stub
		
		// 1.Download list of listed companies 
		
		//ListOfTSEListedDownloadImpl.start();
		
		// 2.Download historical quotes
		
		HistoricalQuoteDownload.start();
		
	}

}
