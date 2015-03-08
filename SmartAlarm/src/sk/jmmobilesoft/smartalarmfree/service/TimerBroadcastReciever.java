package sk.jmmobilesoft.smartalarmfree.service;

import sk.jmmobilesoft.smartalarmfree.TimerRingActivity;
import sk.jmmobilesoft.smartalarmfree.helpers.Helper;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TimerBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.serviceInfo("TimerBroadcastReciever: onRecieve");
		Helper.wakeLockOn(context);
		Intent timer = new Intent(context, TimerRingActivity.class);
		timer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		timer.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		timer.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		timer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		timer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		timer.putExtras(intent);

		context.startActivity(timer);
	}

}
