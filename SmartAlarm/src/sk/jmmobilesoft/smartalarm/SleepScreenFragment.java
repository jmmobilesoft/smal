package sk.jmmobilesoft.smartalarm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SleepScreenFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sleep_screen_fragment, container,
				false);

		Button wifion = (Button) view.findViewById(R.id.image_button_wifi_on);
		wifion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				turnWifiOn();

			}
		});
		Button wifioff = (Button) view.findViewById(R.id.image_button_wifi_off);
		wifioff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				turnWifiOff();

			}
		});
		Button mobileon = (Button) view.findViewById(R.id.image_button_mobile_on);
		mobileon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				turnMobileOn();

			}
		});
		Button mobileoff = (Button) view.findViewById(R.id.image_button_mobile_off);
		mobileoff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				turnMobileOff();

			}
		});
		return view;
	}

	private void turnWifiOn() {
		WifiManager wManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		wManager.setWifiEnabled(true);
	}

	private void turnWifiOff() {
		WifiManager wManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		wManager.setWifiEnabled(false);
	}

	private void turnMobileOn() {
		ConnectivityManager dataManager;
		dataManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
	
	private void turnMobileOff(){
		ConnectivityManager dataManager;
		dataManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
