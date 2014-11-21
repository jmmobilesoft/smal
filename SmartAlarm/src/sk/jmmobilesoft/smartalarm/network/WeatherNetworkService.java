package sk.jmmobilesoft.smartalarm.network;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.weather.WeatherHttpClient;
import sk.jmmobilesoft.smartalarm.weather.WeatherJsonParser;

public class WeatherNetworkService {

	private NetworkService network;

	public WeatherNetworkService() {
		network = new NetworkService();
	}

	public List<WeatherForecast> downloadWeather(List<String> cityList,
			Context context) {
		network.turnWifiOn(context);
		connect(context);
		List<WeatherForecast> weather = getWeather(cityList);
		network.turnWifiOff(context);
		return weather;
	}

	public boolean availableWeather(String city) {
		WeatherHttpClient client = new WeatherHttpClient();
		client.getWeatherData(city);
		String s = client.getDataString();
		if (s.contains("Error")) {
			return false;
		}
		return true;
	}

	public List<WeatherForecast> getWeather(List<String> cityList) {
		WeatherHttpClient client = new WeatherHttpClient();
		WeatherJsonParser parser = new WeatherJsonParser();
		List<WeatherForecast> list = new ArrayList<WeatherForecast>();
		WeatherForecast weather = null;
		for (String s : cityList) {
			try {
				weather = null;
				client.getWeatherData(s);
				weather = parser.parseData(client.getDataString());
				if (weather != null) {
					list.add(weather);
				}
			} catch (NullPointerException e) {
				System.out.println(e);
				return null;
			}
		}

		return list;
	}

	public void connect(final Context context) {
		new Connect(context).execute();
	}
	
	public class Connect extends AsyncTask<Void, Void, Void> {

		private Context mContext;
		
		public Connect(Context context){
			mContext = context;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				int counter = 0;
				while (!network.isConnected(mContext) && counter <= 60) {
					System.out.println("waiting:" + counter);
					counter++;
					Thread.yield();
				}

			} catch (Exception e) {
				System.out.println(e);
				//network.turnWifiOff(mContext);
			}
			return null;
		}		
	}
}
