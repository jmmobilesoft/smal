package sk.jmmobilesoft.smartalarm.service;

import sk.jmmobilesoft.smartalarm.TimerRingScreen;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimerServiceReciever extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("INFO", "TimerServiceReciever: onRecieve");
		Helper.wakeLockOn(context);
		Intent timerIntent = new Intent(context, TimerRingScreen.class);
		timerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		timerIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		timerIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		timerIntent.putExtras(intent);
		context.startActivity(timerIntent);
	}
}
