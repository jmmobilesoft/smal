package sk.jmmobilesoft.smartalarm.service;

import java.util.Calendar;

import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

@SuppressLint("NewApi")
public class WeatherRegularRefreshService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.serviceInfo("WeatherRegularRefreshService: onStartCommand");
		int refreshTime = Integer.valueOf(GlobalHelper.getStringPreference(
				getApplicationContext(), "automatic_refresh", "0"));
		Calendar weatherSet = Helper.getCurrentTime();
		weatherSet.add(Calendar.HOUR_OF_DAY, refreshTime);
		System.out.println(weatherSet.getTime());

		System.out.println("TEST REFRESH TIME: " + refreshTime);
		PendingIntent weatherService = PendingIntent.getService(
				getApplicationContext(), (int) 34, new Intent(
						getApplicationContext(),
						WeatherRegularRefreshService.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent weather = new Intent(getApplicationContext(),
				WeatherRefreshService.class);
		getApplicationContext().startService(weather);

		stopSelf();

		AlarmManager alarmMgr = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		if (refreshTime != 0) {
			alarmMgr.setExact(AlarmManager.RTC_WAKEUP,
					weatherSet.getTimeInMillis(), weatherService);
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Logger.serviceInfo("WeatherRegularRefreshService: onDestroy");
		super.onDestroy();
	}
}