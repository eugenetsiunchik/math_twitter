package by.eugenetsiunchik.twitteritto.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class DateUtility {

	private static final String minuteAgo = " minute ago";
	private static final String minutesAgo = " minutes ago";
	private static final String hourAgo = " hour ago";
	private static final String houresAgo = " houres ago";
	private static final String dayAgo = " day ago";
	private static final String daysAgo = " days ago";
	private static final String longTimeAgo = " a long time ago...";
	private static final String onlyJust = " only just";

	public static String getDateDifference(String thenDateString) {

		String stringDateFormat = "EEE MMM dd HH:mm:ss z yyyy";
		SimpleDateFormat format = new SimpleDateFormat(stringDateFormat,
				Locale.ENGLISH);
		Date thenDate;
		try {
			thenDate = format.parse(thenDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		Calendar now = Calendar.getInstance();

		Calendar then = Calendar.getInstance();
		now.setTime(new Date());
		then.setTime(thenDate);

		// Get the represented date in milliseconds
		long nowMs = now.getTimeInMillis() - 3 * 60 * 60 * 1000;
		long thenMs = then.getTimeInMillis();

		// Calculate difference in milliseconds
		long diff = nowMs - thenMs;

		// Calculate difference in seconds
		long diffSeconds = diff / (1000);
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);

		if (diffSeconds < 60) {
			return (String) onlyJust ;
		} else if (diffMinutes < 60) {
			return (String) ((diffMinutes == 1) ? diffMinutes + minuteAgo
					: diffMinutes + minutesAgo);
		} else if (diffHours < 24) {
			return (diffHours == 1) ? diffHours + hourAgo : diffHours
					+ houresAgo;
		} else if (diffDays < 30) {
			return ((diffDays == 1) ? diffDays + dayAgo : diffDays + daysAgo);
		} else {
			return longTimeAgo;
		}
	}
}
