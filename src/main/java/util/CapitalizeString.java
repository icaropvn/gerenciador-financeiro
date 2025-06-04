package util;

public class CapitalizeString {
	public static String capitalize(String string) {
		if(string == null || string.isEmpty())
			return string;
		
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
}
