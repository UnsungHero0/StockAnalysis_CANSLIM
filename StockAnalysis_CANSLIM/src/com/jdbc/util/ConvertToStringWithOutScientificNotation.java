package com.jdbc.util;

import java.text.DecimalFormat;

public class ConvertToStringWithOutScientificNotation {

	public ConvertToStringWithOutScientificNotation() {
		// TODO Auto-generated constructor stub
	}
	
	public static String covert(Float input) {
		DecimalFormat df = new DecimalFormat("#################################.#######################");
		return df.format(input);
	}
	
	public static String covert(Double input) {
		DecimalFormat df = new DecimalFormat("#################################.#######################");
		return df.format(input);
	}

}
