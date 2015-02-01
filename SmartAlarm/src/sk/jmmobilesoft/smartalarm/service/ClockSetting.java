package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;
import java.util.List;

import sk.jmmobilesoft.smartalarm.ClockRingActivity;
import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.ClockHelper;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ClockSetting {

	@SuppressLint("NewApi")
	public static boolean setClock(Context context, long id) {
		DBHelper db = new DBHelper(context);
		boolean nextday = false;

		Calendar current = Helper.getCurrentTime();

		Clock c = db.getClock(id);
		System.out.println("budik na nastavenie:" + c);
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
		PendingIntent weather = weatherPendingIntent(context, c);
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		ClockHelper.determineAlarmIcon(context);
		if (c.isActive() && Helper.getDayRepeat(c, nextday)) {
			aManager.setExact(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pIntent);
			Calendar weatherC = Calendar.getInstance();
			weatherC.setTimeInMillis(calendar.getTimeInMillis() - 570000);
			Logger.setInfo("Setting clock: " + c + " - ACTIVE \nfor:"
					+ calendar.getTime());
			if ((weatherC.getTimeInMillis() - 60000) > Helper.getCurrentTime()
					.getTimeInMillis()) {
				aManager.setExact(AlarmManager.RTC_WAKEUP,
						weatherC.getTimeInMillis(), weather);
				Logger.serviceInfo("Setting weather refresh for:"
						+ weatherC.getTime());
			}else Logger.serviceInfo("Weather refresh not set");
			return true;
		} else {
			aManager.cancel(pIntent);
			aManager.cancel(weather);
			Logger.setInfo("Setting clock: " + c + " - DEACTIVE");
			return false;
		}
	}

	@SuppressLint("NewApi")
	public static void setSnoozeClock(Context context, long id) { // TODO Clock
																	// Snooze
																	// screen /
																	// edit
																	// original
																	// screen
		DBHelper db = new DBHelper(context);
		Clock c = db.getClock(id);

		Calendar current = Helper.getCurrentTime();

		// long time = current.getTimeInMillis() + (1000 * 60 *
		// c.getSnoozeTime());
		current.add(Calendar.MINUTE, c.getSnoozeTime());
		PendingIntent pIntent = createPendingIntent(context, c);
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (android.os.Build.VERSION.SDK_INT < 19) {
			aManager.setExact(AlarmManager.RTC_WAKEUP,
					current.getTimeInMillis(), pIntent);
		} else {
			aManager.setExact(AlarmManager.RTC_WAKEUP,
					current.getTimeInMillis(), pIntent);
			Logger.setInfo("Setting clock: " + c + " - ACTIVE");
		}
		System.out.println("setting snooze for:" + current);
	}

	public static void setAllClocks(Context context) {
		Logger.appInfo("Setting all clock");
		DBHelper db = new DBHelper(context);
		List<Clock> clocks = db.getClocks();
		for (Clock c : clocks) {
			setClock(context, c.getId());
		}
	}

	public static void deactivateClock(Clock c, Context context) {
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pIntent = createPendingIntent(context, c);
		aManager.cancel(pIntent);
	}

	private static PendingIntent createPendingIntent(Context context, Clock c) {
		Intent intent = new Intent(context, ClockRingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.putExtra("ID", c.getId());

		return PendingIntent.getActivity(context, (int) c.getId(), intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

	}

	// TODO EXTRACT away to another class???
	private static PendingIntent weatherPendingIntent(Context context, Clock c) {
		Intent intent = new Intent(context, WeatherRefreshService.class);
		intent.putExtra("ID", c.getId());

		return PendingIntent.getService(context, (int) c.getId(), intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
	}
}
