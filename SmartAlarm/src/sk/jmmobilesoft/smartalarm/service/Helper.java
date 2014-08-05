package sk.jmmobilesoft.smartalarm.service;

import android.content.Context;
import android.os.PowerManager;

public abstract class Helper {

	public static String format(int value) {
		String ret = "";
		if (value < 10) {
			ret = "0";
		}
		ret += Integer.toString(value);
		return ret;
	}
	
	private static PowerManager.WakeLock wl = null;
	
	public static void wakeLockOn(Context context){
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		if (wl == null)
			wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "CLOCK_ALARM");
		wl.acquire();
	}
	
	public static void wakeLockOff(Context context){
		try {
			if (wl != null)
				wl.release();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
}
