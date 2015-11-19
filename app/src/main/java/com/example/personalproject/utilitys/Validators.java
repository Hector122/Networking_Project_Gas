package com.example.personalproject.utilitys;

public class Validators {

	/**
	 * Indicate true is one of the string pass in the param is null or empty. 
	 * @param txtVariable
	 *            String to verify.
	 * @return boolean
	 * */
	public static boolean isNullOrEmpty(String... txtVariable) {
		for (String text : txtVariable) {
			if (text == null || text.isEmpty() || text.equals("")) {
				return true;
			}
		}
		return false;
	}
}
