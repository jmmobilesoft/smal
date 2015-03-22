package sk.jmmobilesoft.smartalarm.network;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Weather;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;

public class WeatherNetworkService {

	private NetworkService network;

	public WeatherNetworkService() {
		network = new NetworkService();
	}

	public void refreshAllWeather(final Context mContext){
		DBHelper db = new DBHelper(mContext);
		WeatherNetworkService service = new WeatherNetworkService();
		List<String> cityList = new ArrayList<>();
		for (WeatherForecast w : db.getWeatherForecast()) {
			cityList.add(w.getCityName());
		}
		List<WeatherForecast> weathers = service
				.downloadWeatherForecast(cityList);
		if (weathers != null) {
			for (WeatherForecast w : weathers) {
				WeatherForecast up = db.getWeatherForecastByCity(w
						.getCityName());
				if (up != null) {
					w.setId(up.getId());
					db.updateWeatherForecast(w);
				} else {
					db.createWeatherForecast(w);
				}
			}
		}
		List<Weather> weather = service.downloadWeather(mContext, cityList);
		if (weathers != null) {
			db.deleteAllWeather();
			for (Weather w : weather) {
				db.createWeather(w);
			}
		}
	}
	
	
	
	public List<WeatherForecast> downloadAutomaticWeather(List<String> cityList,
			Context context) {
		network.turnWifiOn(context);
		new Connect(context).execute();
		List<WeatherForecast> weather = downloadWeatherForecast(cityList);
		network.turnWifiOff(context);
		return weather;
	}

	public boolean availableWeather(String city) {
		try {
			WeatherHttpClient client = new WeatherHttpClient();
			client.getWeatherForecastData(city);
			String s = client.getWeatherForecastString();
			if (s.contains("Error")) {
				return false;
			}
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}

	public List<WeatherForecast> downloadWeatherForecast(List<String> cityList) {
		WeatherHttpClient client = new WeatherHttpClient();
		WeatherJsonParser parser = new WeatherJsonParser();
		List<WeatherForecast> list = new ArrayList<WeatherForecast>();
		WeatherForecast weather = null;
		for (String s : cityList) {
			try {
				weather = null;
				s = s.replace(" ", "_");
				client.getWeatherForecastData(s);
				weather = parser.parseWeatherForecastData(client.getWeatherForecastString());
				if (weather != null) {
					list.add(weather);
				}
			} catch (NullPointerException e) {
				Logger.logStackTrace(e.getStackTrace());
				return null;
			}
		}

		return list;
	}
	
	public List<Weather> downloadWeather(Context context, List<String> cityList) {
		WeatherHttpClient client = new WeatherHttpClient();
		WeatherJsonParser parser = new WeatherJsonParser();
		List<Weather> list = new ArrayList<Weather>();
		for (String s : cityList) {
			try {
				client.getWeatherData(s);
				List<Weather> w = parser.parseWeatherData(client.getWeatherString());
				if (!w.isEmpty()) {
					list.addAll(w);
				}
			} catch (NullPointerException e) {
				Logger.logStackTrace(e.getStackTrace());
				return null;
			}
		}
		SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = s.edit();
		e.putLong("update", Helper.getCurrentTime().getTimeInMillis());
		e.commit();
		return list;
	}

	public class Connect extends AsyncTask<Void, Void, Void> {

		private Context mContext;

		public Connect(Context context) {
			mContext = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				int counter = 0;
				while (!network.isConnected(mContext) && counter <= 60) {
					counter++;
					Thread.yield();
				}

			} catch (Exception e) {
				Logger.logStackTrace(e.getStackTrace());
			}
			return null;
		}
	}
}
