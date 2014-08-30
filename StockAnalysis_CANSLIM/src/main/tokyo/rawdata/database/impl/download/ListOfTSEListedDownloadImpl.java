package impl.download;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;
import datasource.DataSourceUtil;
import module.ListOfTSEListed.ListOfTSEListedDownload;
import namespace.DBNameSpace;

public class ListOfTSEListedDownloadImpl {
	
	private static final DataSource dataSource = DataSourceUtil.DINGUNSW();
	
	public ListOfTSEListedDownloadImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args) {
		start();
	}

	public static void start() {

		System.out.println(DBNameSpace.getListedcompaniesTokyoDb()
				+ " download is started!");
		Long startTime = Calendar.getInstance().getTimeInMillis();
		
		run();
		Long endTime = Calendar.getInstance().getTimeInMillis();
		Integer minute = (int) ((endTime - startTime) / (long)(1000 * 60));
		Integer second = (int)((endTime - startTime) / (long)(1000)) % 60;

		System.out.println(DBNameSpace.getListedcompaniesTokyoDb()
				+ " download is finished!");
		System.out.println("running time : " + minute + " minutes " + second + " seconds");

	}

	public static void run() {

		Connection con = null;
		try {
			con = dataSource.getConnection();
			ListOfTSEListedDownload.downloadListedCompanyList(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
