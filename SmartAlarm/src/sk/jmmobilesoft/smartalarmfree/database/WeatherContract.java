package sk.jmmobilesoft.smartalarmfree.database;

import android.provider.BaseColumns;

public abstract class WeatherContract {

public WeatherContract() {}
	
	public static abstract class WeatherModel implements BaseColumns {
		public static final String TABLE_NAME = "weather";
		public static final String WEATHER_REQUESTNAME = "request"; 
		public static final String WEATHER_CITYNAME = "city"; 
		public static final String WEATHER_DESCRIPTION = "description";
		public static final String WEATHER_ICON = "icon";
		public static final String WEATHER_TEMPMAX = "tempmax";
		public static final String WEATHER_TEMPMIN = "tempmin";
		public static final String WEATHER_DATE = "date";
	}
	
}
