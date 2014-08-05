package sk.jmmobilesoft.smartalarm.service;

import android.app.Service;
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
		//getApplicationContext().sendBroadcast(new Intent(getApplicationContext(),ClockServiceReciever.class).putExtras(intent));
		System.out.println("CLOCK SERVICE");
		return super.onStartCommand(intent, flags, startId);
	}
}
