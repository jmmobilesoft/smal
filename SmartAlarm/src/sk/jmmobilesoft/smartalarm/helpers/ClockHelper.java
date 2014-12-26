package sk.jmmobilesoft.smartalarm.helpers;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.content.Context;
import android.content.Intent;

public class ClockHelper {

	public static void determineAlarmIcon(Context context) {
		DBHelper db = new DBHelper(context);
		
		boolean someActive = false;
		for (Clock c: db.getClocks()) {
			if (c.isActive()) {
				someActive = true;
			}
		}
		setAlarmIcon(context, someActive);
	}
	
	private static void setAlarmIcon(Context context, boolean enabled) {
		Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
		alarmChanged.putExtra("alarmSet", enabled);
		context.sendBroadcast(alarmChanged);
	}
	
}
