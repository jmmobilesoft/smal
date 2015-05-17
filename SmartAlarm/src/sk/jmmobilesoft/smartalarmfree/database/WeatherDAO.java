package sk.jmmobilesoft.smartalarmfree.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import sk.jmmobilesoft.smartalarmfree.database.WeatherContract.WeatherModel;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import sk.jmmobilesoft.smartalarmfree.model.Weather;

public class WeatherDAO {

	private static final String SQL_CREATE_TABLE = "CREATE TABLE "
			+ WeatherModel.TABLE_NAME + " (" + WeatherModel._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ WeatherModel.WEATHER_CITYNAME + " TEXT,"
			+ WeatherModel.WEATHER_REQUESTNAME + " TEXT,"
			+ WeatherModel.WEATHER_DESCRIPTION + " TEXT,"
			+ WeatherModel.WEATHER_ICON + " TEXT,"
			+ WeatherModel.WEATHER_TEMPMAX + " TEXT,"
			+ WeatherModel.WEATHER_TEMPMIN + " TEXT,"
			+ WeatherModel.WEATHER_DATE + " TEXT" + ")";

	private static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST "
			+ WeatherModel.TABLE_NAME;

	public void createTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	public void dropTable(SQLiteDatabase db) {
		db.execSQL(SQL_DROP_TABLE);
	}

	public void deleteAll(SQLiteDatabase db) {
		db.execSQL("delete from " + WeatherModel.TABLE_NAME);
	}

	public void deleteWeatherByCity(SQLiteDatabase db, String city) {
		db.execSQL("delete from " + WeatherModel.TABLE_NAME + " WHERE "
				+ WeatherModel.WEATHER_CITYNAME + " = " + "'" + city + "'");
	}

	public long createWeather(SQLiteDatabase db, Weather weather) {
		ContentValues values = populateWeatherContent(weather);
		long id = db.insert(WeatherModel.TABLE_NAME, null, values);
		db.close();
		return id;
	}

	public long updateWeather(SQLiteDatabase db, Weather weather) {
		ContentValues values = populateWeatherContent(weather);
		long id = db.update(WeatherModel.TABLE_NAME, values, WeatherModel._ID
				+ "=?", new String[] { String.valueOf(weather.getId()) });
		db.close();
		return id;
	}

	public int deleteWeather(SQLiteDatabase db, long id) {
		int rows = db.delete(WeatherModel.TABLE_NAME, WeatherModel._ID + " =?",
				new String[] { String.valueOf(id) });
		return rows;
	}

	public Weather getWeather(SQLiteDatabase db, long id) {
		try {
			String select = "SELECT * FROM " + WeatherModel.TABLE_NAME
					+ " WHERE " + WeatherModel._ID + " = " + id;

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

	public Weather getWeatherByCityDate(SQLiteDatabase db, String city,
			String date) {
		String select = "SELECT * FROM " + WeatherModel.TABLE_NAME + " WHERE "
				+ WeatherModel.WEATHER_CITYNAME + " = " + "'" + city + "' AND "
				+ WeatherModel.WEATHER_DATE + " = " + "'" + date + "'";

		Cursor c = db.rawQuery(select, null);

		Weather weather = new Weather();

		if (c.moveToNext()) {
			weather = populateWeatherModel(c);
		}
		db.close();
		return weather;
	}

	public List<Weather> getWeather(SQLiteDatabase db) {

		String select = "SELECT * FROM " + WeatherModel.TABLE_NAME;

		Cursor c = db.rawQuery(select, null);

		List<Weather> weatherList = new ArrayList<Weather>();

		while (c.moveToNext()) {
			weatherList.add(populateWeatherModel(c));
		}
		db.close();
		return weatherList;
	}

	private Weather populateWeatherModel(Cursor c) {
		Weather weather = new Weather();
		weather.setId(c.getLong(c.getColumnIndex(WeatherModel._ID)));
		weather.setCity(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_CITYNAME)));
		weather.setRequestName(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_REQUESTNAME)));
		weather.setDescription(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_DESCRIPTION)));
		weather.setIcon(c.getString(c.getColumnIndex(WeatherModel.WEATHER_ICON)));
		weather.setTempMax(c.getInt(c
				.getColumnIndex(WeatherModel.WEATHER_TEMPMAX)));
		weather.setTempMin(c.getInt(c
				.getColumnIndex(WeatherModel.WEATHER_TEMPMIN)));
		weather.setDate(c.getString(c.getColumnIndex(WeatherModel.WEATHER_DATE)));

		return weather;
	}

	private ContentValues populateWeatherContent(Weather weather) {
		ContentValues values = new ContentValues();
		values.put(WeatherModel.WEATHER_CITYNAME, weather.getCity());
		values.put(WeatherModel.WEATHER_REQUESTNAME, weather.getRequestName());
		values.put(WeatherModel.WEATHER_DESCRIPTION, weather.getDescription());
		values.put(WeatherModel.WEATHER_ICON, weather.getIcon());
		values.put(WeatherModel.WEATHER_TEMPMAX, weather.getTempMax());
		values.put(WeatherModel.WEATHER_TEMPMIN, weather.getTempMin());
		values.put(WeatherModel.WEATHER_DATE, weather.getDate());
		return values;
	}
}
