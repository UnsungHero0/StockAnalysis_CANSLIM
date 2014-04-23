package impl.download;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.sql.DataSource;

import module.annualfinancialstatement.FinancialStatementYahooJDBCCreateTable;
import module.annualfinancialstatement.FinancialStatementYahooJDBCUpdateData;
import datasource.DataSourceUtil;
import jdbcdao.CodeListsDao;

/**
 * 1.check if the table of financial statement is exist 2.if not, create the
 * table 3.get all the code list in TSE 4.loop:{ 5.check if there is much new
 * information for this code 6.if yes, insert (update) the data into the table}
 * 
 * @author Daytona
 * 
 */

public class FinancialStatementYahooJDBCImpl {

	public FinancialStatementYahooJDBCImpl() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) throws IOException, ParseException {

		// drop the table
		// new FinancialStatementYahooJDBCCreateTable()
		// .DropFinancialStatementTable();
		// create the table
		new FinancialStatementYahooJDBCCreateTable()
				.FinancialStatementYahooJDBCCreateTableImpl();

		// new FinancialStatementYahooJDBCUpdateData()
		// .FinancialStatemetYahooJDBCUpdateDataImpl("1853");
		// get code list from table section_tokyo
		ArrayList<String> codeList = new ArrayList<>();
		codeList = new CodeListsDao().getCodeLists();
		System.out.println("all record is : " + codeList.size());
		Integer count = 0;
		Connection con;
		try {
			con = DataSourceUtil.getTokyoDataSourceRoot().getConnection();
			for (String code : codeList) {
				// check and update the data
				System.out.print(code + " start... ");
				if (Integer.valueOf(code) >= 0) {
					FinancialStatementYahooJDBCUpdateData
							.FinancialStatemetYahooJDBCUpdateDataImpl(code, con);
				}
				System.out.println("OK! " + (codeList.size() - (++count))
						+ " to go");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DataSource getDataSource() {
		return DataSourceUtil.getTokyoDataSourceRoot();
	}

}
