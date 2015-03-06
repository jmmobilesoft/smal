package sk.jmmobilesoft.smartalarm.service;

import sk.jmmobilesoft.smartalarm.log.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.serviceInfo("AutoStartReciever: onRecieve");
		Intent service = new Intent(context, ClockRepeatService.class);
		context.startService(service);
	}

}
