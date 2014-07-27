package sk.jmmobilesoft.smartalarm.service;

import sk.jmmobilesoft.smartalarm.RingScreen;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClockServiceReciever extends BroadcastReceiver{

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("CLOCK SERVICE RECIEVER");
		Intent clockIntent = new Intent(context, RingScreen.class);
		clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		clockIntent.putExtras(intent);	
		context.startActivity(clockIntent);
		
	}
}
