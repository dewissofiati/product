package util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.springframework.util.StringUtils;

public class Utility {
	public static int tryParse(String string) {
		if (string == null || StringUtils.isEmpty(string)) {
			return 0;
		} else {
			return Integer.parseInt(string);
		}
	}

	public static boolean isNotNull(String param) {
		return (param != null && !StringUtils.isEmpty(param) && !Objects.equals(param, "null")) ? true : false;
	}

	public static Date stringToDate(String param, String pattern) {
		if (isNotNull(param)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault());
			return Date.valueOf(LocalDate.parse(param.replace("-", ""), formatter));
		} else {
			return new Date(System.currentTimeMillis());
		}

	}
}
