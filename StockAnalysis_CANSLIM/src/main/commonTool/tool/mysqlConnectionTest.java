package tool;

import java.sql.Connection;
import java.sql.SQLException;

import module.listedcompanydownload.DownloadHistoricalQuotesSydneyImpl;
import datasource.DataSourceUtil;
import impl.listedcompanydownload.DownloadHistoricalQuotesSydney;


public class mysqlConnectionTest {

	public static void main(String args[])  {
		Connection con = null;
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();

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
		System.out.println("finished");
	}

}
