package sk.jmmobilesoft.smartalarm;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_Holo);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_activity);
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
