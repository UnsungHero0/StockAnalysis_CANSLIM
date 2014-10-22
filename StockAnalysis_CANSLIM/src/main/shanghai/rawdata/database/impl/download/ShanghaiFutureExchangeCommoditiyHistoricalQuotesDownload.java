package impl.download;

import java.sql.Connection;
import java.sql.SQLException;

import module.shanghaifutureexchange.ShanghaiFutureExchangeCommoditityHistoricalQuotesDwonloadImpl;
import datasource.DataSourceUtil;

public class ShanghaiFutureExchangeCommoditiyHistoricalQuotesDownload {
	private static Connection con = null;

	public static void main(String args[]) {

		start();

	}

	public static void start() {
		ShanghaiFutureExchangeCommoditityHistoricalQuotesDwonloadImpl.impl(con);
//		try {
//			con = DataSourceUtil.DINGUNSW().getConnection();
//			ShanghaiFutureExchangeCommoditityHistoricalQuotesDwonloadImpl.impl(con);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			if (con != null) {
//				try {
//					con.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
}
