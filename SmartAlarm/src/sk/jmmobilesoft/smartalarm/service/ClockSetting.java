package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ClockSetting {

	@SuppressLint("NewApi")
	public static void setClock(Context context, long id) {
		DBHelper db = new DBHelper(context);
		boolean nextday = false;
		
		Calendar current = Helper.getCurrentTime();

		Clock c = db.getClock(id);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, c.getHour());
		calendar.set(Calendar.MINUTE, c.getMinutes());
		calendar.set(Calendar.SECOND, 0);

		if (calendar.before(current)) {
			calendar.set(Calendar.DATE,
					Calendar.getInstance().get(Calendar.DATE) + 1);
			nextday = true;
		} else {
			calendar.set(Calendar.DATE,
					Calendar.getInstance().get(Calendar.DATE));
		}
		PendingIntent pIntent = createPendingIntent(context, c);
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (c.isActive() && getDayRepeat(c, nextday)) {
			if (android.os.Build.VERSION.SDK_INT < 19) {
				aManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pIntent);
			} else {
				aManager.setExact(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pIntent);
			}
			Logger.setInfo("Setting clock: " + c + " - ACTIVE");
		} else {
			aManager.cancel(pIntent);
			Logger.setInfo("Setting clock: " + c + " - DEACTIVE");
		}
	}

	public static void setAllClocks(Context context) {
		Logger.appInfo("Setting all clock");
		DBHelper db = new DBHelper(context);
		List<Clock> clocks = db.getClocks();
		for (Clock c : clocks) {
			setClock(context, c.getId());
		}
	}

	private static PendingIntent createPendingIntent(Context context, Clock c) {
		Intent intent = new Intent(context, ClockServiceReciever.class);
		intent.putExtra("ID", c.getId());
		intent.putExtra("NAME", c.getName());
		intent.putExtra("TIME_HOUR", c.getHour());
		intent.putExtra("TIME_MINUTE", c.getMinutes());
		intent.putExtra("TONE", c.getSound());

		return PendingIntent.getBroadcast(context, (int) c.getId(), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

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

	private static boolean noRepeats(int[] repeats) {
		for (int i = 0; i <= 6; i++) {
			if (repeats[i] == 1) {
				return false;
			}
		}
		return true;
	}

}
