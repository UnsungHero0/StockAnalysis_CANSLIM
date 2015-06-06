package impl.listedcompanydownload;

import java.sql.Connection;
import java.sql.SQLException;

import module.listedcompanydownload.UpdateListedCompanySydeny;
import datasource.DataSourceUtil;

public class UpdateListedCompanySydenyImpl {
	
	private static Connection con = null;

	public static void main(String args[]) {
		start();
	}

	public static void start() {
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			UpdateListedCompanySydeny.updateListedCompanyList(con);
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
