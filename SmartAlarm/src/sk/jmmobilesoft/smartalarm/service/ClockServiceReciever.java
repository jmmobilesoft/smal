package sk.jmmobilesoft.smartalarm.service;

import sk.jmmobilesoft.smartalarm.ClockRingActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ClockServiceReciever extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("INFO", "ClockServiceReciever: onRecieve");
		Helper.wakeLockOn(context);
		Intent clockIntent = new Intent(context, ClockRingActivity.class);
		clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		clockIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		clockIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		clockIntent.putExtras(intent);	
		context.startActivity(clockIntent);
	}
}
