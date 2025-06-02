package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransformarStringEmData {
	public static Date StringToDate(String stringDate) {
		Date data = null;
		
		try {
			String pattern = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			
			data = sdf.parse(stringDate);
		}
		catch(ParseException e) {
			e.printStackTrace();
		}
		
		return data;
	}
}
