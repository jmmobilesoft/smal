package sk.jmmobilesoft.smartalarm.helpers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.content.Context;
import android.content.Intent;

public class ClockHelper {

	public static void determineAlarmIcon(Context context) {
		DBHelper db = new DBHelper(context);

		boolean someActive = false;
		for (Clock c : db.getClocks()) {
			if (c.isActive()) {
				someActive = true;
			}
		}
		setAlarmIcon(context, someActive);
	}

	public static Calendar calendarFromString(String s) {
		try {
			String[] split = s.split(",");
			String[] date = split[0].split("\\.");
			String[] time = split[1].split(":");
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0].trim()));
			c.set(Calendar.MINUTE, Integer.valueOf(time[1].trim()));
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[0].trim()));
			c.set(Calendar.MONTH, (Integer.valueOf(date[1].trim()) - 1));
			c.set(Calendar.YEAR, Integer.valueOf(date[2].trim()));
			return c;
		} catch (Exception e) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, -1);
			return c;
		}
	}

	private static void setAlarmIcon(Context context, boolean enabled) {
		Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
		alarmChanged.putExtra("alarmSet", enabled);
		context.sendBroadcast(alarmChanged);
	}

}
