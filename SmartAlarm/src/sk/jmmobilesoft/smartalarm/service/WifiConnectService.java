package sk.jmmobilesoft.smartalarm.service;

import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode.VmPolicy;
import android.provider.Settings;

public class WifiConnectService extends Service {

	public static WifiLock wifiLock = null;

	public static WakeLock wakeLock = null;

	private NetworkService nService;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.serviceInfo("WifiConnectService: onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		Logger.serviceInfo("WifiConnectService: onCreate");
		acquireWakeLock();
		connectWifi();
		super.onCreate();
	}

	private void acquireWakeLock() {
		PowerManager pm = (PowerManager) getApplicationContext()
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WeatherRefresh");
		wakeLock.setReferenceCounted(false);
		wakeLock.acquire();
		Logger.serviceInfo("WifiConnectService: wake lock acquired");
	}

	private void connectWifi() {
		nService = new NetworkService();
		WifiManager wManager = (WifiManager) getApplicationContext()
				.getSystemService(Context.WIFI_SERVICE);
		if (android.os.Build.VERSION.SDK_INT > 12)
			wifiLock = wManager.createWifiLock(
					WifiManager.WIFI_MODE_FULL_HIGH_PERF, "RefreshLock");
		else
			wifiLock = wManager.createWifiLock(WifiManager.WIFI_MODE_FULL,
					"RefreshLock");
		wifiLock.setReferenceCounted(false);
		wifiLock.acquire();
		wManager.setWifiEnabled(true);
		Logger.serviceInfo("WifiConnectService: wifi lock acquired");
	}
	
	@Override
	public void onDestroy() {
		Logger.serviceInfo("WifiConnectService: onDestroy");
		super.onDestroy();
	}
}
