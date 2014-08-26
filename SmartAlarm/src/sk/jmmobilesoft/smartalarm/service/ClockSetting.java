package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
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

		Calendar current = Calendar.getInstance();
		current.set(Calendar.DAY_OF_WEEK,
				Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		current.set(Calendar.HOUR_OF_DAY,
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, Calendar.getInstance()
				.get(Calendar.MINUTE));
		current.set(Calendar.SECOND, Calendar.getInstance()
				.get(Calendar.SECOND));

		Clock c = db.getClock(id);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, c.getHour());
		calendar.set(Calendar.MINUTE, c.getMinutes());
		calendar.set(Calendar.SECOND, 0);

		if (calendar.before(current)) {
			calendar.set(Calendar.DATE,
					Calendar.getInstance().get(Calendar.DATE) + 1);
		} else {
			calendar.set(Calendar.DATE,
					Calendar.getInstance().get(Calendar.DATE));
		}
		PendingIntent pIntent = createPendingIntent(context, c);
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (c.isActive()) {
			if (android.os.Build.VERSION.SDK_INT < 19) {
				aManager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pIntent);
			} else {
				aManager.setExact(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), pIntent);
			}
		} else {
			aManager.cancel(pIntent);
		}
	}

	public static void setAllClocks(Context context) {
		DBHelper db = new DBHelper(context);
		List<Clock> clocks = db.getClocks();
		for (Clock c: clocks) {
			if (getDayRepeat(c)) {
				setClock(context, c.getId());
			}
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

	public static boolean getDayRepeat(Clock c) {
		int[] repeats = c.getRepeat();
		int[] converted = new int[]{repeats[6],repeats[0],repeats[1],repeats[2],repeats[3],repeats[4],repeats[5]};
		int currentDay = getCurrentDay();
		if (converted[currentDay - 1] == 1 || converted.equals(new int[]{0,0,0,0,0,0,0})) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return days 1-7 int value SU -> SA 1 -> 7 !!!
	 */
	public static int getCurrentDay() {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		return day;
	}

}
