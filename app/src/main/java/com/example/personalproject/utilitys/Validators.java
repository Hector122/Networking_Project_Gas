package com.example.personalproject.utilitys;

public class Validators {

	/**
	 * Indicate true is one of the string pass in the param is null or empty. 
	 * @param txtVarible
	 *            String to verify.
	 * @return boolean
	 * */
	public static boolean isNullOrEmpy(String... txtVarible) {
		for (String text : txtVarible) {
			if (text == null || text.isEmpty() || text.equals("")) {
				return true;
			}
		}
		return false;
	}
}
