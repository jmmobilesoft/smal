package sk.jmmobilesoft.smartalarm.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpClient {

	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

	private static String IMG_URL = "http://openweathermap.org/img/w/";

	public String getWeatherData(String location) {
		HttpURLConnection connnection = null;
		InputStream is = null;
		
		try {
			connnection = (HttpURLConnection) (new URL(BASE_URL + location))
					.openConnection();
			connnection.setRequestMethod("GET");
			connnection.setDoInput(true);
			connnection.setDoOutput(true);
			connnection.connect();

			StringBuffer sb = new StringBuffer();
			is = connnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line + "\r\n");
			is.close();
			connnection.disconnect();
			return sb.toString();
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
		return null;

	}

}
