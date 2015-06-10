package impl.listedcompanydownload;

import java.sql.Connection;
import java.sql.SQLException;

import module.listedcompanydownload.DownLoadListedCompanyNewYork;
import datasource.DataSourceUtil;

public class DownLoadListedCompaniesListNewYorkImpl {
	
	private static Connection con = null;

	public static void main(String args[]) {
		start();
	}

	public static void start() {
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			DownLoadListedCompanyNewYork.downloadListedCompanyList(con);

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
