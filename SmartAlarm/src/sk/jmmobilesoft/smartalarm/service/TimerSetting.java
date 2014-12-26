package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;

import sk.jmmobilesoft.smartalarm.TimerRingScreen;
import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.model.Timer;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class TimerSetting {

	@SuppressLint("NewApi")
	public static void setTimer(Context context, long id) {
		DBHelper db = new DBHelper(context);
		Timer t = db.getTimer(id);

		Calendar timer = Helper.getCurrentTime();

		timer.add(Calendar.SECOND, t.getSeconds());
		timer.add(Calendar.MINUTE, t.getMinutes());
		timer.add(Calendar.HOUR_OF_DAY, t.getHours());

		PendingIntent pIntent = createPendingIntent(context, t);
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (t.isActive()) {
				aManager.setExact(AlarmManager.RTC_WAKEUP,
						timer.getTimeInMillis(), pIntent);
		} else {
			aManager.cancel(pIntent);
		}
	}

	public static void deactivateTimer(Timer t, Context context) {
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pIntent = createPendingIntent(context, t);
		aManager.cancel(pIntent);
	}

	private static PendingIntent createPendingIntent(Context context, Timer t) {
		Intent intent = new Intent(context, TimerRingScreen.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.putExtra("ID", t.getId());

		return PendingIntent.getActivity(context, (int) t.getId(), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

	}

}
