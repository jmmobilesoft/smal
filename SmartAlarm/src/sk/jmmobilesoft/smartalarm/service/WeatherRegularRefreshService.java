package sk.jmmobilesoft.smartalarm.service;

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
		int refreshTime = intent.getIntExtra("refresh_time", 0);

		Intent weather = new Intent(getApplicationContext(),
				WeatherRefreshService.class);

		getApplicationContext().startService(weather);

		PendingIntent weatherService = PendingIntent.getService(
				getApplicationContext(), (int) 34, new Intent(
						getApplicationContext(),
						WeatherRegularRefreshService.class),
				PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmMgr = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		if (refreshTime != 0) {
			alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, refreshTime
					* 60 * 60 * 60 * 1000, weatherService);
		}

		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Logger.serviceInfo("WeatherRegularRefreshService: onDestroy");
		super.onDestroy();
	}
}