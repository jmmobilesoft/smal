package sk.jmmobilesoft.smartalarm.helpers;

import android.content.Context;

public class WeatherHelper {

	public static String getTemperature(Context context, float temperature) {
		String units = GlobalHelper.getStringPreference(context, "measurements", "M");
		if (units.equals("M")) {
			return Helper.kelvinToCelsius(temperature) + "°" + "C";
		}
		if (units.equals("I")) {
			return Helper.kelvinToFarenheit(temperature) + "°" + "F";
		}
		return Helper.kelvinToCelsius(temperature) + "°" + "C";
	}
	
	public static String getWindSpeed(Context context, float speed){
		String units = GlobalHelper.getStringPreference(context, "measurements", "M");
		if (units.equals("M")) {
			return speed + " m/s";
		}
		if (units.equals("I")) {
			return Math.round((speed * 1.943844492) * 10) / 10 + " kn";
		}
		return speed + " m/s";
	}
}
