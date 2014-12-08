package sk.jmmobilesoft.smartalarm.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient {

	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
	
	private static String LONG_URL1 = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";

	private static String LONG_URL2 = "&mode=json&units=metric&cnt=4";
	
	private static String result;
	
	private static String weatherResult;

	public String getWeatherForecastString() {
		return result;
	}
	
	public String getWeatherString(){
		return weatherResult;
	}

	public void getWeatherForecastData(final String location) {
		Thread t = new Thread() {
			@Override
			public void run() {
				HttpURLConnection connnection = null;
				InputStream is = null;
				
				try {
					connnection = (HttpURLConnection) (new URL(BASE_URL
							+ location)).openConnection();
					connnection.setRequestMethod("GET");
					connnection.setDoInput(true);
					connnection.setDoOutput(true);
					connnection.connect();

					StringBuffer sb = new StringBuffer();
					is = connnection.getInputStream();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null)
						sb.append(line + "\r\n");
					is.close();
					connnection.disconnect();
					result = sb.toString();
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (Throwable t) {
					}
					try {
						connnection.disconnect();
					} catch (Throwable t) {
					}
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getWeatherData(final String location) {
		Thread t = new Thread() {
			@Override
			public void run() {
				HttpURLConnection connnection = null;
				InputStream is = null;
				
				try {
					connnection = (HttpURLConnection) (new URL(LONG_URL1
							+ location + LONG_URL2)).openConnection();
					connnection.setRequestMethod("GET");
					connnection.setDoInput(true);
					connnection.setDoOutput(true);
					connnection.connect();

					StringBuffer sb = new StringBuffer();
					is = connnection.getInputStream();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null)
						sb.append(line + "\r\n");
					is.close();
					connnection.disconnect();
					weatherResult = sb.toString();
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (Throwable t) {
					}
					try {
						connnection.disconnect();
					} catch (Throwable t) {
					}
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
