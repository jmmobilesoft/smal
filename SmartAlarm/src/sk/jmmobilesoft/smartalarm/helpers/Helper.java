package sk.jmmobilesoft.smartalarm.helpers;

import java.util.Calendar;
import java.util.Date;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.NumberPicker.Formatter;
import android.widget.Toast;

public abstract class Helper {

	public static String format(int value) {
		String ret = "";
		if (value < 10) {
			ret = "0";
		}
		ret += Integer.toString(value);
		return ret;
	}

	private static PowerManager.WakeLock wl = null;

	public static void wakeLockOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		if (wl == null)
			wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP, "CLOCK_ALARM");
		wl.acquire();
	}

	public static void wakeLockOff(Context context) {
		try {
			if (wl != null)
				wl.release();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}


	public static Calendar getCurrentTime() {
		Calendar current = Calendar.getInstance();
		current.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		current.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
		current.set(Calendar.DAY_OF_MONTH,
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		current.set(Calendar.DAY_OF_WEEK,
				Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		current.set(Calendar.HOUR_OF_DAY,
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, Calendar.getInstance()
				.get(Calendar.MINUTE));
		current.set(Calendar.SECOND, Calendar.getInstance()
				.get(Calendar.SECOND));
		return current;
	}

	public static void createToast(Context context, String message) {
		Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		t.show();
	}

	public static void showToast(Clock c, Context context) {
		Calendar clock = Calendar.getInstance();
		clock.set(Calendar.HOUR_OF_DAY, c.getHour());
		clock.set(Calendar.MINUTE, c.getMinutes());
		clock.set(Calendar.SECOND, 0);
		Calendar current = getCurrentTime();
		if (clock.before(current)) {
			clock.set(Calendar.DATE,
					Calendar.getInstance().get(Calendar.DATE) + 1);
		}

		long timeToParse = clock.getTimeInMillis() - current.getTimeInMillis();
		timeToParse = timeToParse / 1000 / 60;
		int mins = (int) (timeToParse % 60);
		int hours = (int) (timeToParse / 60);
		String text = "Alarm will fire in " + hours + "h " + mins + "m.";
		Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		t.show();
	}

	public static float kelvinToCelsius(float temperature) {
		float cels = temperature - 273.15f;
		return Math.round(cels * 10) / 10;
	}

	public static String milisToTime(long milliseconds) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds * 1000);
		return format(c.get(Calendar.HOUR_OF_DAY)) + ":"
				+ format(c.get(Calendar.MINUTE));
	}

	public static Date milisToDate(long miliseconds) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(miliseconds * 1000);
		return c.getTime();
	}

	public static boolean getDayRepeat(Clock c, boolean nextday) {
		int[] repeats = c.getRepeat();
		int[] converted = new int[] { repeats[6], repeats[0], repeats[1],
				repeats[2], repeats[3], repeats[4], repeats[5] };
		int currentDay = getCurrentDay();
		int nextdayInt = getCurrentDay() + 1;
		if (nextdayInt == 7) {
			nextdayInt = nextdayInt - 7;
		}
		if (nextday) {
			if (converted[nextdayInt] == 1 || noRepeats(converted)) {
				nextday = false;
				return true;
			}
		} else {
			if (converted[currentDay] == 1 || noRepeats(converted)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return days 0-6 int value SU -> SA 1 -> 7 !!!
	 */
	public static int getCurrentDay() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		return day - 1;
	}

	public static boolean noRepeats(int[] repeats) {
		for (int i = 0; i <= 6; i++) {
			if (repeats[i] == 1) {
				return false;
			}
		}
		return true;
	}
}
