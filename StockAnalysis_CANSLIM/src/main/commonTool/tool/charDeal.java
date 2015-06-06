package tool;

public class charDeal {

	public static String subComma(String input) {
		String result = "";
		char[] inputList = input.toCharArray();
		for (int i = 0; i < inputList.length; i++) {
			if (!String.valueOf(inputList[i]).equals(",")) {
				result += inputList[i];
			}
		}
		return result;
	}
	
	public static String subSpecificChar(String input, String target) {
		String result = "";
		char[] inputList = input.toCharArray();
		for (int i = 0; i < inputList.length; i++) {
			if (!String.valueOf(inputList[i]).equals(target)) {
				result += inputList[i];
			}
		}
		return result;
	}

	public static Boolean hasDigital(String input) {
		Boolean result = false;
		for (char ele : input.toCharArray()) {
			result = Character.isDigit(ele);
			if (result == true) {
				break;
			}
		}
		return result;
	}

	public static Boolean ifhasChar(String input) {
		Boolean result = false;
		for (char ele : input.toCharArray()) {
			String.valueOf(ele).equals(input);
			if (result == true) {
				break;
			}
		}
		return result;
	}

	public static String extractDigital(String input) {
		String result = "";
		for (char ele : input.toCharArray()) {
			if (Character.isDigit(ele)) {
				result += String.valueOf(ele);
			}
		}
		return result;
	}

	public static Integer countMark(String input, String mark) {
		Integer result = 0;
		if (input.contains(mark)) {
			String[] stringList = input.split(mark);
			result = stringList.length - 1;
		}
		return result;
	}

}
