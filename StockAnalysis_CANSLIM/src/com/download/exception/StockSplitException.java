package com.download.exception;

public class StockSplitException  extends Exception{

	/**
	 * when the stock is spited, the price would be adjusted, so that , the quotes should be updated totally
	 */
	private static final long serialVersionUID = 5728601962348030736L;

	public StockSplitException(){
		super();
	}
	
	public StockSplitException(String msg) {
		super(msg);
	}

}
