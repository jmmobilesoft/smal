package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;

import sk.jmmobilesoft.smartalarm.database.ClockDBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class ClockService extends Service {

	public static String TAG = ClockService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		getApplicationContext().sendBroadcast(new Intent(getApplicationContext(),ClockServiceReciever.class).putExtras(intent));
		System.out.println("CLOCK SERVICE");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@SuppressLint("NewApi")
	public static void updateClock(Context context, long id){
		ClockDBHelper db = new ClockDBHelper(context);
		
		Calendar current = Calendar.getInstance();
		current.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		current.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		current.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND));
		
		Clock c = db.getClock(id);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, c.getHour());
		calendar.set(Calendar.MINUTE, c.getMinutes());
		calendar.set(Calendar.SECOND, 0);
		
		if(calendar.before(current)){
			calendar.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE) + 1);
		}
		else{
			calendar.set(Calendar.DATE, Calendar.getInstance().get(Calendar.DATE));
		}
		PendingIntent pIntent = createPendingIntent(context, c);
		AlarmManager aManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if(c.isActive()){
			if(android.os.Build.VERSION.SDK_INT < 19){
			aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
			}
			else{
				aManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
			}
		}
		else{
			aManager.cancel(pIntent);
		}
	}
	
	private static PendingIntent createPendingIntent(Context context, Clock c){
		Intent intent = new Intent(context, ClockService.class);
		intent.putExtra("ID", c.getId());
		intent.putExtra("NAME", c.getName());
		intent.putExtra("TIME_HOUR", c.getHour());
		intent.putExtra("TIME_MINUTE", c.getMinutes());
		intent.putExtra("TONE", c.getSound());
		
		return PendingIntent.getService(context, (int)c.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
	}

}
