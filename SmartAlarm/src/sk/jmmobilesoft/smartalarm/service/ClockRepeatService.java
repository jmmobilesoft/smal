package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;

import sk.jmmobilesoft.smartalarm.log.Logger;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ClockRepeatService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.serviceInfo("ClockRepeatService started");
		ClockSetting.setAllClocks(this);
		setRepeatIntent();
		return START_STICKY;
	}
	
	@SuppressLint("NewApi")
	private void setRepeatIntent(){
		Intent repeat = new Intent(this, ClockRepeatService.class);
		PendingIntent pRepeat = PendingIntent.getService(this, 24, repeat, PendingIntent.FLAG_UPDATE_CURRENT);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 01);
		if (c.before(getCurrentTime())) {
			c.set(Calendar.DATE,
					Calendar.getInstance().get(Calendar.DATE) + 1);
		} else {
			c.set(Calendar.DATE,
					Calendar.getInstance().get(Calendar.DATE));
		}
		AlarmManager aManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		if(android.os.Build.VERSION.SDK_INT < 19){
			aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pRepeat);
		}
		else{
			aManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pRepeat);
		}
	}
	
	private Calendar getCurrentTime(){
		Calendar current = Calendar.getInstance();
		current.set(Calendar.HOUR_OF_DAY,
				Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		current.set(Calendar.MINUTE, Calendar.getInstance()
				.get(Calendar.MINUTE));
		current.set(Calendar.SECOND, Calendar.getInstance()
				.get(Calendar.SECOND));
		return current;
	}

}
