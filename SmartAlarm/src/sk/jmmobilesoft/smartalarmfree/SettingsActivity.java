package sk.jmmobilesoft.smartalarmfree;

import sk.jmmobilesoft.smartalarmfree.R;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import sk.jmmobilesoft.smartalarmfree.service.WeatherRegularRefreshService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private static final String AUTOREFRESH_KEY = "automatic_refresh";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_activity);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		sp.registerOnSharedPreferenceChangeListener(this);
		Preference button = (Preference)findPreference("button_buy");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		                @Override
		                public boolean onPreferenceClick(Preference arg0) { 
		                	Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri
									.parse("market://details?id=sk.jmmobilesoft.smartalarm"));
							startActivity(intent);
		                    return true;
		                }
		            });
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Logger.serviceInfo("key:" + key);
		Logger.serviceInfo("equals:" + key.equals(AUTOREFRESH_KEY));
		if(key.equals(AUTOREFRESH_KEY)){
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			int refreshTime = Integer.valueOf(sp.getString(AUTOREFRESH_KEY, "0"));
			Logger.serviceInfo("refresh time:" + refreshTime);
			Intent service = new Intent(this, WeatherRegularRefreshService.class);
			startService(service);
		}
		
	}
    
	
	
//	private String getWeatherString() {
//		WeatherHttpClient client = new WeatherHttpClient();
//		WeatherJsonParser parser = new WeatherJsonParser();
//		WeatherForecast weather = null;
//		try {
//			client.getWeatherForecastData("Myjava");
//			weather = parser.parseWeatherForecastData(client.getWeatherForecastString());
//		} catch (NullPointerException e) {
//			System.out.println(e);
//			return "cant download weather";
//		}
//		System.out.println("weather:" + weather);
//		System.out.println(weather.getSunrise() + "," + weather.getSunset());
//
//		String fReturn = new String("city:          " + weather.getCityName()
//				+ " \ntemp:          "
//				+ Helper.kelvinToCelsius(weather.getTemperature())
//				+ " \ndescription:   " + weather.getMainDesc()
//				+ " \nhumidity:      " + weather.getHumidity()
//				+ " \nsunrise:       "
//				+ Helper.milisToTime(weather.getSunrise())
//				+ " \nsunset:        "
//				+ Helper.milisToTime(weather.getSunset()));
//		return fReturn;
//	}
//
//	public class Connect extends AsyncTask<Void, Void, Void> {
//
//		private Context mContext;
//
//		public Connect(Context context) {
//			mContext = context;
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			try {
//				network.turnWifiOn(getApplicationContext());
//				int counter = 0;
//				while (!network.isConnected(mContext) && counter <= 60) {
//					counter++;
//					Thread.sleep(1000);
//					System.out.println("waiting:" + counter);
//				}
//
//			} catch (Exception e) {
//				System.out.println(e);
//				// network.turnWifiOff(mContext);
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			if (network.isConnected(mContext)) {
//				weather.setText(getWeatherString());
//			}
//			network.turnWifiOff(getApplicationContext());
//			super.onPostExecute(result);
//		}
//	}

}
