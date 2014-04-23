package module.canslimanalysis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import jdbcdao.SingleItemDaoFromDB;

public class FinancialStatementAnalysisFetchDBData {

	public FinancialStatementAnalysisFetchDBData() {
		// TODO Auto-generated constructor stub
	}
	
	public HashMap<String, FinancialStatementAnalysisRecord> fetchDataFromDB(HashMap<String, FinancialStatementAnalysisRecord> inputrecord, String item, Connection con) {
		HashMap<String, String> fetchResult = null;
		ArrayList<String> codeList = new ArrayList<>(inputrecord.keySet());
		SingleItemDaoFromDB.fetchDataFromFinancialStatement(codeList, item, con);
		String getMethodName = "get" + item;
		String setMethodName = "set" + item;
		Class returnType = null;
		Method method = null;
		for (Method m : new FinancialStatementAnalysisRecord().getClass().getMethods()) {
			if(getMethodName.toUpperCase().equals(m.getName().toUpperCase())) {
				try {
					returnType = Class.forName(m.getReturnType().toString());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			} else if (setMethodName.toUpperCase().equals(m.getName().toUpperCase())) {
				method = m;
			}
		}
		for (String code : inputrecord.keySet()) {
			try {
				method.invoke(inputrecord.get(code), inputrecord.get(code));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//TODO
		return inputrecord;
	}

}
