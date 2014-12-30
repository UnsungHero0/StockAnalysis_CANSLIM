package impl.download;

/**
 * download stock index futures Top 20 holders with largest volume of turnover/buy/sell
 * the source data is from http://www.cffex.com.cn/fzjy/ccpm/
 * the data URL is "http://www.cffex.com.cn/fzjy/ccpm/201412/10/IF.xml" detected by Charles (MacOS)
 * loop the data in URL to get all the source from  2010/4/16
 * data format in Mysql will be date/
 * @author Daytona
 */

import java.sql.Connection;
import java.sql.SQLException;

import module.shanghaifutureexchange.ShanghaiFutureExchangeCommoditityHistoricalQuotesDwonloadImpl;
import datasource.DataSourceUtil;

public class ShanghaiStockIndexExchangeHistoricalBuySellTurnoverVolumeDownload {
	private static Connection con = null;

	public static void main(String args[]) {

		start();

	}

	public static void start() {
		try {
			con = DataSourceUtil.DINGUNSW().getConnection();
			ShanghaiFutureExchangeCommoditityHistoricalQuotesDwonloadImpl.impl(con);

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
