package sk.jmmobilesoft.smartalarm.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sk.jmmobilesoft.smartalarm.log.Logger;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class NetworkService {

	public NetworkService() {
	}

	public boolean wifiAllowed(Context context) {
		WifiManager wManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wManager.isWifiEnabled();
	}

	public boolean mobileAllowed(Context context) {
		ConnectivityManager dataManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = dataManager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public void turnWifiOn(Context context) {
		WifiManager wManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wManager.setWifiEnabled(true);
		wManager.startScan();
	}

	public void turnWifiOff(Context context) {
		WifiManager wManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		wManager.setWifiEnabled(false);
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

		Logger.appInfo("isConnected: " + networkInfo == null ? networkInfo
				.toString() : "networkinfo null");
		return networkInfo != null && networkInfo.isConnected();
	}
}
