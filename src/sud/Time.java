package sud;

public class Time {
	private static int minutes;
	private static int hours;
	private static int day;
	private static int month;
	private static int year;

	public static int getMinutes() {
		return minutes;
	}

	public static void setMinutes(int minutes) {
		Time.minutes = minutes;
	}

	public static int getHours() {
		return hours;
	}

	public static void setHours(int hours) {
		Time.hours = hours;
	}

	public static int getDay() {
		return day;
	}

	public static void setDay(int day) {
		Time.day = day;
	}

	public static int getMonth() {
		return month;
	}

	public static void setMonth(int month) {
		Time.month = month;
	}

	public static int getYear() {
		return year;
	}

	public static void setYear(int year) {
		Time.year = year;
	}

	public static void tick() {
		minutes++;
		if (minutes == 60) {
			minutes = 0;
			hours++;
		}
		if (hours == 24) {
			hours = 0;
			day++;
		}
		if (day > 30) {
			day = 1;
			month++;
		}
		if (month > 12) {
			month = 1;
			year++;
		}
	}

	public static String getTime() {
		return "<font color=white>" + day + "." + month + "." + year + " "
				+ hours + ":" + minutes + "</font><br>";
	}
}
