package sk.jmmobilesoft.smartalarm.network;

import android.content.Context;
import sk.jmmobilesoft.smartalarm.weather.WeatherHttpClient;
import sk.jmmobilesoft.smartalarm.weather.WeatherJsonParser;
import sk.jmmobilesoft.smartalarm.weather.model.WeatherForecast;

public class WeatherNetworkService {

	private NetworkService network;
	
	public WeatherNetworkService(){
		network = new NetworkService();
	}
	
	
	public WeatherForecast downloadWeather(String city, Context context){
		network.turnWifiOn(context);
		connect(context);
		WeatherForecast weather = getWeather(city);
		network.turnWifiOff(context);
		return weather;
	}
	
	private WeatherForecast getWeather(String city) {
		WeatherHttpClient client = new WeatherHttpClient();
		WeatherJsonParser parser = new WeatherJsonParser();
		WeatherForecast weather = null;
		try {
			client.getWeatherData(city);
			weather = parser.parseData(client.getDataString());
		} catch (NullPointerException e) {
			System.out.println(e);
			return null;
		}
		
		return weather;
	}
	
	private void connect(final Context context) {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					int counter = 0;
					while (!network.isConnected(context) || counter >= 60) {
						Thread.sleep(1000);
						System.out.println("waiting");
						counter++;
					}

				} catch (Exception e) {
					System.out.println(e);
					network.turnWifiOff(context);
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
