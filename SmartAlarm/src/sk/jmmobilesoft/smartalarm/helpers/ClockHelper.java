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
	
	public static boolean compareRepeats(Clock c1, Clock c2){
		if(c1 == null || c2 == null){
			return false;
		}
		for(int i = 0; i < c1.getRepeat().length; i++){
			if(c1.getRepeat()[i] != c2.getRepeat()[i]){
				return false;
			}
		}
		return true;
	}
	
	private static void setAlarmIcon(Context context, boolean enabled) {
		Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
		alarmChanged.putExtra("alarmSet", enabled);
		context.sendBroadcast(alarmChanged);
	}
	
}
