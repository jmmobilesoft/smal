package sk.jmmobilesoft.smartalarm.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class NetworkService {

	private WifiLock wifiLock = null;

	public NetworkService() {
	}

	public void turnWifiOn(Context context) {
		WifiManager wManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (android.os.Build.VERSION.SDK_INT > 12)
			wifiLock = wManager.createWifiLock(
					WifiManager.WIFI_MODE_FULL_HIGH_PERF, "RefreshLock");
		else
			wifiLock = wManager.createWifiLock(WifiManager.WIFI_MODE_FULL,
					"RefreshLock");
		wifiLock.acquire();
		wManager.setWifiEnabled(true);
		wManager.startScan();
	}

	public void turnWifiOff(Context context) {
		WifiManager wManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wManager.setWifiEnabled(false);
		if (wifiLock != null) {
			wifiLock.release();
		}
	}

	public void turnMobileOn(Context context) {
		ConnectivityManager dataManager;
		dataManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			Method dataMtd = ConnectivityManager.class.getDeclaredMethod(
					"setMobileDataEnabled", boolean.class);
			dataMtd.setAccessible(true);
			dataMtd.invoke(dataManager, true);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public void turnMobileOff(Context context) {
		ConnectivityManager dataManager;
		dataManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			Method dataMtd = ConnectivityManager.class.getDeclaredMethod(
					"setMobileDataEnabled", boolean.class);
			dataMtd.setAccessible(false);
			dataMtd.invoke(dataManager, false);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager.getActiveNetworkInfo();
		}

		return networkInfo != null
				&& networkInfo.getState() == NetworkInfo.State.CONNECTED;
	}
}
