package sk.jmmobilesoft.smartalarmfree.database;

import android.provider.BaseColumns;

public abstract class WeatherForecastContract {

	public WeatherForecastContract() {}
	
	public static abstract class WeatherForecastModel implements BaseColumns {
		public static final String TABLE_NAME = "weatherforecast";
		public static final String WEATHER_CITYNAME = "city"; 
		public static final String WEATHER_CLOUDSALL = "clouds";
		public static final String WEATHER_COUNTRY = "country";
		public static final String WEATHER_DESCRIPTION = "decription";
		public static final String WEATHER_HUMIDITY = "humidity";
		public static final String WEATHER_ICON = "icon";
		public static final String WEATHER_LATITUDE = "latitude";
		public static final String WEATHER_LONGITUDE = "longitude";
		public static final String WEATHER_MAINDESC = "maindesc";
		public static final String WEATHER_PRESSURE = "pressure";
		public static final String WEATHER_SUNRISE = "sunrise";
		public static final String WEATHER_SUNSET = "sunset";
		public static final String WEATHER_TEMPMAX = "tempmax";
		public static final String WEATHER_TEMPERATURE = "temperature";
		public static final String WEATHER_TEMPMIN = "tempmin";
		public static final String WEATHER_WINDDEG = "winddeg";
		public static final String WEATHER_WINDSPEED = "windspeed";
		public static final String WEATHER_UPDATETIME = "updateTime";
	}
	
}
