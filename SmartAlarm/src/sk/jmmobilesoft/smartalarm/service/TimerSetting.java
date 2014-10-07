package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Timer;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class TimerSetting {
	
	@SuppressLint("NewApi")
	public static void setTimer(Context context, long id){
		DBHelper db = new DBHelper(context);
		Timer t = db.getTimer(id);
		
		Calendar current = Helper.getCurrentTime();
		
		int hours = t.getHours();
		int minutes = t.getMinutes();
		
		int seconds = t.getSeconds();
		
		current.add(Calendar.SECOND, t.getSeconds());
		current.add(Calendar.MINUTE, t.getMinutes());
		current.add(Calendar.HOUR_OF_DAY, t.getHours());
		
//		Calendar cal = Calendar.getInstance();
//		
//		
//		//TODO check setting
//		if(current.get(Calendar.SECOND) +  seconds > 59){
//			cal.set(Calendar.SECOND, current.get(Calendar.SECOND) +  seconds - 60);
//			minutes += 1;
//		} else {
//			cal.set(Calendar.SECOND, current.get(Calendar.SECOND) + seconds);
//		}
//		if(current.get(Calendar.MINUTE) +  minutes > 59){
//			cal.set(Calendar.MINUTE, current.get(Calendar.MINUTE) +  minutes - 60);
//			hours += 1;
//		} else {
//			cal.set(Calendar.MINUTE, current.get(Calendar.MINUTE) + minutes);
//		}
//		if(current.get(Calendar.HOUR_OF_DAY) +  hours > 23){ //TODO more days
//			int days = hours / 24;
//			hours = hours % 24;
//			System.out.println("days:" + days + "   ,hours:" + hours);
//			cal.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY) +  hours);
//			cal.set(Calendar.DAY_OF_WEEK, current.get(Calendar.DAY_OF_WEEK) + days); //TODO check
//		} else {
//			cal.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY) + hours);
//		}
		
		
		PendingIntent pIntent = createPendingIntent(context, t);
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (t.isActive()) {
			if (android.os.Build.VERSION.SDK_INT < 19) {
				aManager.set(AlarmManager.RTC_WAKEUP,
						current.getTimeInMillis(), pIntent);
			} else {
				aManager.setExact(AlarmManager.RTC_WAKEUP,
						current.getTimeInMillis(), pIntent);
			}
		} else {
			aManager.cancel(pIntent);
		}		
	}
	
	private static PendingIntent createPendingIntent(Context context, Timer t) {
		Intent intent = new Intent(context, TimerServiceReciever.class);
		intent.putExtra("ID", t.getId());

		return PendingIntent.getBroadcast(context, (int) t.getId(), intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

	}
	
}
