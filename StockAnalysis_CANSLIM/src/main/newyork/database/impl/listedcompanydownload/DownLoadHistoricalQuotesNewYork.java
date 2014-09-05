package impl.listedcompanydownload;

import java.sql.Connection;
import java.sql.SQLException;

import module.listedcompanydownload.DownLoadHistoricalQuotesNewYorkImpl;
import datasource.DataSourceUtil;

public class DownLoadHistoricalQuotesNewYork {
	
	private static Connection con = null;

	public static void main(String args[]) {
		start();
	}

	public static void start() {
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			DownLoadHistoricalQuotesNewYorkImpl.downloadHistoricalQuotesNewYorkImpl(con);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
