package sk.jmmobilesoft.smartalarmfree.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import sk.jmmobilesoft.smartalarmfree.database.WeatherForecastContract.WeatherForecastModel;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import sk.jmmobilesoft.smartalarmfree.model.WeatherForecast;

public class WeatherForecastDAO {

	private static final String SQL_CREATE_TABLE = "CREATE TABLE "
			+ WeatherForecastModel.TABLE_NAME + " (" + WeatherForecastModel._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ WeatherForecastModel.WEATHER_CITYNAME + " TEXT,"
			+ WeatherForecastModel.WEATHER_REQUESTNAME + " TEXT,"
			+ WeatherForecastModel.WEATHER_CLOUDSALL + " INTEGER,"
			+ WeatherForecastModel.WEATHER_COUNTRY + " TEXT,"
			+ WeatherForecastModel.WEATHER_DESCRIPTION + " TEXT,"
			+ WeatherForecastModel.WEATHER_HUMIDITY + " INTEGER,"
			+ WeatherForecastModel.WEATHER_ICON + " TEXT,"
			+ WeatherForecastModel.WEATHER_LATITUDE + " TEXT,"
			+ WeatherForecastModel.WEATHER_LONGITUDE + " TEXT,"
			+ WeatherForecastModel.WEATHER_MAINDESC + " TEXT,"
			+ WeatherForecastModel.WEATHER_PRESSURE + " INTEGER,"
			+ WeatherForecastModel.WEATHER_SUNRISE + " TEXT,"
			+ WeatherForecastModel.WEATHER_SUNSET + " TEXT,"
			+ WeatherForecastModel.WEATHER_TEMPERATURE + " TEXT,"
			+ WeatherForecastModel.WEATHER_TEMPMAX + " TEXT,"
			+ WeatherForecastModel.WEATHER_TEMPMIN + " TEXT,"
			+ WeatherForecastModel.WEATHER_UPDATETIME + " TEXT,"
			+ WeatherForecastModel.WEATHER_WINDDEG + " TEXT,"
			+ WeatherForecastModel.WEATHER_WINDSPEED + " TEXT" + ")";

	private static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST "
			+ WeatherForecastModel.TABLE_NAME;

	public void createTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	public void dropTable(SQLiteDatabase db) {
		db.execSQL(SQL_DROP_TABLE);
	}

	public long createWeatherForecast(SQLiteDatabase db, WeatherForecast weather) {
		ContentValues values = populateWeatherContent(weather);
		long id = db.insert(WeatherForecastModel.TABLE_NAME, null, values);
		db.close();
		return id;
	}

	public long updateWeatherForecast(SQLiteDatabase db, WeatherForecast weather) {
		ContentValues values = populateWeatherContent(weather);
		long id = db.update(WeatherForecastModel.TABLE_NAME, values,
				WeatherForecastModel._ID + "=?",
				new String[] { String.valueOf(weather.getId()) });
		db.close();
		return id;
	}

	public int deleteWeatherForecast(SQLiteDatabase db, long id) {
		int rows = db.delete(WeatherForecastModel.TABLE_NAME,
				WeatherForecastModel._ID + " =?",
				new String[] { String.valueOf(id) });
		return rows;
	}

	public WeatherForecast getWeatherForecast(SQLiteDatabase db, long id) {
		try {
			String select = "SELECT * FROM " + WeatherForecastModel.TABLE_NAME
					+ " WHERE " + WeatherForecastModel._ID + " = " + id;

			Cursor c = db.rawQuery(select, null);

			if (c.moveToNext()) {
				db.close(); // TODO think about
				return populateWeatherModel(c);
			}
		} catch (Exception e) {
			Logger.logStackTrace(e.getStackTrace());
		}
		return null;
	}

	public WeatherForecast getWeatherForecastByCity(SQLiteDatabase db,
			String city) {
		String select = "SELECT * FROM " + WeatherForecastModel.TABLE_NAME
				+ " WHERE " + WeatherForecastModel.WEATHER_CITYNAME + " = "
				+ "'" + city + "'";

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			db.close(); // TODO think about
			return populateWeatherModel(c);
		}
		return null;
	}

	public List<WeatherForecast> getWeatherForecast(SQLiteDatabase db) {
		String select = "SELECT * FROM " + WeatherForecastModel.TABLE_NAME;
		Cursor c = db.rawQuery(select, null);
		List<WeatherForecast> weatherList = new ArrayList<WeatherForecast>();
		while (c.moveToNext()) {
			weatherList.add(populateWeatherModel(c));
		}
		db.close();
		return weatherList;
	}

	private WeatherForecast populateWeatherModel(Cursor c) {
		WeatherForecast weather = new WeatherForecast();
		weather.setId(c.getLong(c.getColumnIndex(WeatherForecastModel._ID)));
		weather.setCityName(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_CITYNAME)));
		weather.setRequestName(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_REQUESTNAME)));
		weather.setCloudsAll(c.getInt(c
				.getColumnIndex(WeatherForecastModel.WEATHER_CLOUDSALL)));
		weather.setCountry(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_COUNTRY)));
		weather.setDescription(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_DESCRIPTION)));
		weather.setHumidity(c.getInt(c
				.getColumnIndex(WeatherForecastModel.WEATHER_HUMIDITY)));
		weather.setIcon(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_ICON)));
		weather.setLatitude(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_LATITUDE)));
		weather.setLongitude(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_LONGITUDE)));
		weather.setMainDesc(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_MAINDESC)));
		weather.setPressure(c.getInt(c
				.getColumnIndex(WeatherForecastModel.WEATHER_PRESSURE)));
		weather.setSunrise(c.getLong(c
				.getColumnIndex(WeatherForecastModel.WEATHER_SUNRISE)));
		weather.setSunset(c.getLong(c
				.getColumnIndex(WeatherForecastModel.WEATHER_SUNSET)));
		weather.setTemperature(c.getFloat(c
				.getColumnIndex(WeatherForecastModel.WEATHER_TEMPERATURE)));
		weather.setTempMax(c.getInt(c
				.getColumnIndex(WeatherForecastModel.WEATHER_TEMPMAX)));
		weather.setTempMin(c.getInt(c
				.getColumnIndex(WeatherForecastModel.WEATHER_TEMPMIN)));
		weather.setUpdateTime(c.getString(c
				.getColumnIndex(WeatherForecastModel.WEATHER_UPDATETIME)));
		weather.setWindDeg(c.getFloat(c
				.getColumnIndex(WeatherForecastModel.WEATHER_WINDDEG)));
		weather.setWindSpeed(c.getFloat(c
				.getColumnIndex(WeatherForecastModel.WEATHER_WINDSPEED)));
		return weather;
	}

	private ContentValues populateWeatherContent(WeatherForecast weather) {
		ContentValues values = new ContentValues();
		values.put(WeatherForecastModel.WEATHER_CITYNAME, weather.getCityName());
		values.put(WeatherForecastModel.WEATHER_REQUESTNAME,
				weather.getRequestName());
		values.put(WeatherForecastModel.WEATHER_CLOUDSALL,
				weather.getCloudsAll());
		values.put(WeatherForecastModel.WEATHER_COUNTRY, weather.getCountry());
		values.put(WeatherForecastModel.WEATHER_DESCRIPTION,
				weather.getDescription());
		values.put(WeatherForecastModel.WEATHER_HUMIDITY, weather.getHumidity());
		values.put(WeatherForecastModel.WEATHER_ICON, weather.getIcon());
		values.put(WeatherForecastModel.WEATHER_LATITUDE, weather.getLatitude());
		values.put(WeatherForecastModel.WEATHER_LONGITUDE,
				weather.getLongitude());
		values.put(WeatherForecastModel.WEATHER_MAINDESC, weather.getMainDesc());
		values.put(WeatherForecastModel.WEATHER_PRESSURE, weather.getPressure());
		values.put(WeatherForecastModel.WEATHER_SUNRISE, weather.getSunrise());
		values.put(WeatherForecastModel.WEATHER_SUNSET, weather.getSunset());
		values.put(WeatherForecastModel.WEATHER_TEMPERATURE,
				weather.getTemperature());
		values.put(WeatherForecastModel.WEATHER_TEMPMAX, weather.getTempMax());
		values.put(WeatherForecastModel.WEATHER_TEMPMIN, weather.getTempMin());
		values.put(WeatherForecastModel.WEATHER_UPDATETIME,
				weather.getUpdateTime());
		values.put(WeatherForecastModel.WEATHER_WINDDEG, weather.getWindDeg());
		values.put(WeatherForecastModel.WEATHER_WINDSPEED,
				weather.getWindSpeed());
		return values;
	}
}
