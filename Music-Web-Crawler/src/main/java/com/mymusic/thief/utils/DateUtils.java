package com.mymusic.thief.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static java.sql.Date convertToSqlDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("MM.dd.yyyy");
		Date parsed = null;
		java.sql.Date sqlDate = null;
		try {
			parsed = format.parse(date);
			sqlDate = new java.sql.Date(parsed.getTime());
		} catch (ParseException exc) {
			System.err.println(exc.getMessage());
		}
		return sqlDate;
	}

}
