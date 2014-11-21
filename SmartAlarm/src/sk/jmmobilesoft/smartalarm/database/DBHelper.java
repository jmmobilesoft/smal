package sk.jmmobilesoft.smartalarm.database;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.ClockContract.ClockModel;
import sk.jmmobilesoft.smartalarm.database.TimerContract.TimerModel;
import sk.jmmobilesoft.smartalarm.database.WeatherContract.WeatherModel;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.Timer;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "smartalarm.db";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static final String SQL_DELETE_CLOCK = "DROP TABLE IF EXIST "
			+ ClockModel.TABLE_NAME;

	private static final String SQL_CREATE_CLOCK = "CREATE TABLE "
			+ ClockModel.TABLE_NAME + " (" + ClockModel._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ ClockModel.COLUMN_NAME_CLOCK_NAME + " TEXT,"
			+ ClockModel.COLUMN_NAME_CLOCK_TIME_HOUR + " INTEGER,"
			+ ClockModel.COLUMN_NAME_CLOCK_TIME_MINUTE + " INTEGER,"
			+ ClockModel.COLUMN_NAME_CLOCK_SNOOZE + " INTEGER,"
			+ ClockModel.COLUMN_NAME_CLOCK_REPEAT + " TEXT,"
			+ ClockModel.COLUMN_NAME_CLOCK_TONE + " TEXT,"
			+ ClockModel.COLUMN_NAME_CLOCK_VOLUME + " TEXT,"
			+ ClockModel.COLUMN_NAME_CLOCK_ENABLED + " INTEGER,"
			+ ClockModel.COLUMN_NAME_CLOCK_CITIES + " TEXT" + " )";

	private static final String SQL_DROP_TIMER = "DROP TABLE IF EXIST "
			+ TimerModel.TABLE_NAME;

	private static final String SQL_CREATE_TIMER = "CREATE TABLE "
			+ TimerModel.TABLE_NAME + " (" + TimerModel._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ TimerModel.COLUMN_NAME_TIMER_NAME + " TEXT,"
			+ TimerModel.COLUMN_NAME_TIMER_TIME_HOUR + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_TIME_MINUTE + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_TIME_SECOND + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_ENABLED + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_TONE + " TEXT,"
			+ TimerModel.COLUMN_NAME_TIMER_VOLUME + " TEXT" + ")";

	private static final String SQL_DROP_WEATHER = "DROP TABLE IF EXIST "
			+ WeatherModel.TABLE_NAME;

	private static final String SQL_CREATE_WEATHER = "CREATE TABLE "
			+ WeatherModel.TABLE_NAME + " (" + WeatherModel._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ WeatherModel.WEATHER_CITYNAME + " TEXT,"
			+ WeatherModel.WEATHER_CLOUDSALL + " INTEGER,"
			+ WeatherModel.WEATHER_COUNTRY + " TEXT,"
			+ WeatherModel.WEATHER_DESCRIPTION + " TEXT,"
			+ WeatherModel.WEATHER_HUMIDITY + " INTEGER,"
			+ WeatherModel.WEATHER_ICON + " TEXT,"
			+ WeatherModel.WEATHER_LATITUDE + " TEXT,"
			+ WeatherModel.WEATHER_LONGITUDE + " TEXT,"
			+ WeatherModel.WEATHER_MAINDESC + " TEXT,"
			+ WeatherModel.WEATHER_PRESSURE + " INTEGER,"
			+ WeatherModel.WEATHER_SUNRISE + " TEXT,"
			+ WeatherModel.WEATHER_SUNSET + " TEXT,"
			+ WeatherModel.WEATHER_TEMPERATURE + " TEXT,"
			+ WeatherModel.WEATHER_TEMPMAX + " TEXT,"
			+ WeatherModel.WEATHER_TEMPMIN + " TEXT,"
			+ WeatherModel.WEATHER_UPDATETIME + " TEXT,"
			+ WeatherModel.WEATHER_WINDDEG + " TEXT,"
			+ WeatherModel.WEATHER_WINDSPEED + " TEXT" + ")";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_CLOCK);
		db.execSQL(SQL_CREATE_TIMER);
		db.execSQL(SQL_CREATE_WEATHER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_CLOCK);
		db.execSQL(SQL_DROP_TIMER);
		db.execSQL(SQL_DROP_WEATHER);
		onCreate(db);
	}

	public long createClock(Clock clock) {
		ContentValues values = populateClockContent(clock);
		long id = getWritableDatabase().insert(ClockModel.TABLE_NAME, null,
				values);
		close();
		return id;
	}

	public long updateClock(Clock clock) {
		ContentValues values = populateClockContent(clock);
		long id = getWritableDatabase().update(ClockModel.TABLE_NAME, values,
				ClockModel._ID + " =?",
				new String[] { String.valueOf(clock.getId()) });
		close();
		return id;
	}

	public Clock getClock(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String select = "SELECT * FROM " + ClockModel.TABLE_NAME + " WHERE "
				+ ClockModel._ID + " = " + id;

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			db.close();
			return populateClockModel(c);
		}
		db.close();
		return null;
	}

	public List<Clock> getClocks() {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + ClockModel.TABLE_NAME;

		Cursor c = db.rawQuery(select, null);

		List<Clock> clockList = new ArrayList<Clock>();

		while (c.moveToNext()) {
			clockList.add(populateClockModel(c));
		}
		db.close();
		return clockList;
	}

	public int deleteClock(long id) {
		int rows = getWritableDatabase().delete(ClockModel.TABLE_NAME,
				ClockModel._ID + " =?", new String[] { String.valueOf(id) });
		close();
		return rows;
	}

	private Clock populateClockModel(Cursor c) {
		Clock clock = new Clock();
		clock.setId(c.getLong(c.getColumnIndex(ClockModel._ID)));
		clock.setName(c.getString(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_NAME)));
		clock.setHour(c.getInt(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TIME_HOUR)));
		clock.setMinutes(c.getInt(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TIME_MINUTE)));
		clock.setSnoozeTime(c.getInt(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_SNOOZE)));
		clock.setActive(c.getInt(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_ENABLED)) == 1 ? true
				: false);
		clock.setSound(c.getString(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TONE)) != "" ? Uri
				.parse(c.getString(c
						.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TONE)))
				: null);
		clock.setVolume(c.getFloat(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_VOLUME)));
		clock.setRepeat(clock.fromDBRepeat(c.getString(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_REPEAT))));
		clock.setCities(citiesFromDB(c.getString(c
				.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_CITIES))));
		return clock;
	}

	private ContentValues populateClockContent(Clock clock) {
		ContentValues values = new ContentValues();
		values.put(ClockModel.COLUMN_NAME_CLOCK_NAME, clock.getName());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TIME_HOUR, clock.getHour());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TIME_MINUTE, clock.getMinutes());
		values.put(ClockModel.COLUMN_NAME_CLOCK_SNOOZE, clock.getSnoozeTime());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TONE,
				clock.getSound() != null ? clock.getSound().toString() : "");
		values.put(ClockModel.COLUMN_NAME_CLOCK_VOLUME, clock.getVolume());
		values.put(ClockModel.COLUMN_NAME_CLOCK_ENABLED, clock.isActive() ? 1
				: 0);
		values.put(ClockModel.COLUMN_NAME_CLOCK_REPEAT,
				clock.toDBRepeat(clock.getRepeat()));
		values.put(ClockModel.COLUMN_NAME_CLOCK_CITIES,
				citiesToDB(clock.getCities()));
		return values;
	}

	public long createTimer(Timer timer) {
		ContentValues values = populateTimerContent(timer);
		long id = getWritableDatabase().insert(TimerModel.TABLE_NAME, null,
				values);
		close();
		return id;
	}

	public long updateTimer(Timer timer) {
		ContentValues values = populateTimerContent(timer);
		long id = getWritableDatabase().update(TimerModel.TABLE_NAME, values,
				TimerModel._ID + "=?",
				new String[] { String.valueOf(timer.getId()) });
		close();
		return id;
	}

	public Timer getTimer(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String select = "SELECT * FROM " + TimerModel.TABLE_NAME + " WHERE "
				+ TimerModel._ID + " = " + id;

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			db.close(); // TODO think about
			return populateTimerModel(c);
		}
		return null;
	}

	public List<Timer> getTimers() {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + TimerModel.TABLE_NAME;

		Cursor c = db.rawQuery(select, null);

		List<Timer> timerList = new ArrayList<Timer>();

		while (c.moveToNext()) {
			timerList.add(populateTimerModel(c));
		}
		db.close();
		return timerList;
	}

	public int deleteTimer(long id) {
		int rows = getWritableDatabase().delete(TimerModel.TABLE_NAME,
				TimerModel._ID + " =?", new String[] { String.valueOf(id) });
		return rows;
	}

	private Timer populateTimerModel(Cursor c) {
		Timer timer = new Timer();
		timer.setId(c.getLong(c.getColumnIndex(TimerModel._ID)));
		timer.setName(c.getString(c
				.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_NAME)));
		timer.setHours(c.getInt(c
				.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_TIME_HOUR)));
		timer.setMinutes(c.getInt(c
				.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_TIME_MINUTE)));
		timer.setSeconds(c.getInt(c
				.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_TIME_SECOND)));
		timer.setActive(c.getInt(c
				.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_ENABLED)) == 1 ? true
				: false);
		timer.setSound(c.getString(c
				.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_TONE)) != "" ? Uri
				.parse(c.getString(c
						.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_TONE)))
				: null);
		timer.setVolume(c.getFloat(c
				.getColumnIndex(TimerModel.COLUMN_NAME_TIMER_VOLUME)));
		return timer;
	}

	private ContentValues populateTimerContent(Timer timer) {
		ContentValues values = new ContentValues();
		values.put(TimerModel.COLUMN_NAME_TIMER_NAME, timer.getName());
		values.put(TimerModel.COLUMN_NAME_TIMER_TIME_HOUR, timer.getHours());
		values.put(TimerModel.COLUMN_NAME_TIMER_TIME_MINUTE, timer.getMinutes());
		values.put(TimerModel.COLUMN_NAME_TIMER_TIME_SECOND, timer.getSeconds());
		values.put(TimerModel.COLUMN_NAME_TIMER_ENABLED, timer.isActive() ? 1
				: 0);
		values.put(TimerModel.COLUMN_NAME_TIMER_TONE,
				timer.getSound() != null ? timer.getSound().toString() : "");
		values.put(TimerModel.COLUMN_NAME_TIMER_VOLUME, timer.getVolume());
		return values;
	}

	private WeatherForecast populateWeatherModel(Cursor c) {
		WeatherForecast weather = new WeatherForecast();
		weather.setId(c.getLong(c.getColumnIndex(WeatherModel._ID)));
		weather.setCityName(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_CITYNAME)));
		weather.setCloudsAll(c.getInt(c
				.getColumnIndex(WeatherModel.WEATHER_CLOUDSALL)));
		weather.setCountry(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_COUNTRY)));
		weather.setDecsription(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_DESCRIPTION)));
		weather.setHumidity(c.getInt(c
				.getColumnIndex(WeatherModel.WEATHER_HUMIDITY)));
		weather.setIcon(c.getString(c.getColumnIndex(WeatherModel.WEATHER_ICON)));
		weather.setLatitude(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_LATITUDE)));
		weather.setLongitude(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_LONGITUDE)));
		weather.setMainDesc(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_MAINDESC)));
		weather.setPressure(c.getInt(c
				.getColumnIndex(WeatherModel.WEATHER_PRESSURE)));
		weather.setSunrise(c.getLong(c
				.getColumnIndex(WeatherModel.WEATHER_SUNRISE)));
		weather.setSunset(c.getLong(c
				.getColumnIndex(WeatherModel.WEATHER_SUNSET)));
		weather.setTemperature(c.getFloat(c
				.getColumnIndex(WeatherModel.WEATHER_TEMPERATURE)));
		weather.setTempMax(c.getInt(c
				.getColumnIndex(WeatherModel.WEATHER_TEMPMAX)));
		weather.setTempMin(c.getInt(c
				.getColumnIndex(WeatherModel.WEATHER_TEMPMIN)));
		weather.setUpdateTime(c.getString(c
				.getColumnIndex(WeatherModel.WEATHER_UPDATETIME)));
		weather.setWindDeg(c.getFloat(c
				.getColumnIndex(WeatherModel.WEATHER_WINDDEG)));
		weather.setWindSpeed(c.getFloat(c
				.getColumnIndex(WeatherModel.WEATHER_WINDSPEED)));
		return weather;
	}

	private ContentValues populateWeatherContent(WeatherForecast weather) {
		ContentValues values = new ContentValues();
		values.put(WeatherModel.WEATHER_CITYNAME, weather.getCityName());
		values.put(WeatherModel.WEATHER_CLOUDSALL, weather.getCloudsAll());
		values.put(WeatherModel.WEATHER_COUNTRY, weather.getCountry());
		values.put(WeatherModel.WEATHER_DESCRIPTION, weather.getDecsription());
		values.put(WeatherModel.WEATHER_HUMIDITY, weather.getHumidity());
		values.put(WeatherModel.WEATHER_ICON, weather.getIcon());
		values.put(WeatherModel.WEATHER_LATITUDE, weather.getLatitude());
		values.put(WeatherModel.WEATHER_LONGITUDE, weather.getLongitude());
		values.put(WeatherModel.WEATHER_MAINDESC, weather.getMainDesc());
		values.put(WeatherModel.WEATHER_PRESSURE, weather.getPressure());
		values.put(WeatherModel.WEATHER_SUNRISE, weather.getSunrise());
		values.put(WeatherModel.WEATHER_SUNSET, weather.getSunset());
		values.put(WeatherModel.WEATHER_TEMPERATURE, weather.getTemperature());
		values.put(WeatherModel.WEATHER_TEMPMAX, weather.getTempMax());
		values.put(WeatherModel.WEATHER_TEMPMIN, weather.getTempMin());
		values.put(WeatherModel.WEATHER_UPDATETIME, weather.getUpdateTime());
		values.put(WeatherModel.WEATHER_WINDDEG, weather.getWindDeg());
		values.put(WeatherModel.WEATHER_WINDSPEED, weather.getWindSpeed());
		return values;
	}

	public long createWeather(WeatherForecast weather) {
		ContentValues values = populateWeatherContent(weather);
		long id = getWritableDatabase().insert(WeatherModel.TABLE_NAME, null,
				values);
		close();
		return id;
	}

	public long updateWeather(WeatherForecast weather) {
		ContentValues values = populateWeatherContent(weather);
		long id = getWritableDatabase().update(WeatherModel.TABLE_NAME, values,
				WeatherModel._ID + "=?",
				new String[] { String.valueOf(weather.getId()) });
		close();
		return id;
	}

	public WeatherForecast getWeather(long id) {
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			String select = "SELECT * FROM " + WeatherModel.TABLE_NAME
					+ " WHERE " + WeatherModel._ID + " = " + id;

			Cursor c = db.rawQuery(select, null);

			if (c.moveToNext()) {
				db.close(); // TODO think about
				return populateWeatherModel(c);
			}
			return null;
		} catch (Exception e) {
			StackTraceElement[] s = e.getStackTrace();
			for (int i = 0; i < s.length; i++) {
				Logger.appInfo(s[i].toString());
			}
			throw e;
		}
	}

	public WeatherForecast getWeatherByCity(String city) {
		SQLiteDatabase db = this.getReadableDatabase();
		String select = "SELECT * FROM " + WeatherModel.TABLE_NAME + " WHERE "
				+ WeatherModel.WEATHER_CITYNAME + " = " + "'" + city + "'";

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			db.close(); // TODO think about
			return populateWeatherModel(c);
		}
		return null;
	}

	public List<WeatherForecast> getWeather() {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + WeatherModel.TABLE_NAME;

		Cursor c = db.rawQuery(select, null);

		List<WeatherForecast> weatherList = new ArrayList<WeatherForecast>();

		while (c.moveToNext()) {
			weatherList.add(populateWeatherModel(c));
		}
		db.close();
		return weatherList;
	}

	public int deleteWeather(long id) {
		int rows = getWritableDatabase().delete(WeatherModel.TABLE_NAME,
				WeatherModel._ID + " =?", new String[] { String.valueOf(id) });
		return rows;
	}

	private String citiesToDB(List<String> s) {
		if (s.size() == 1) {
			return s.get(0);
		}
		if (s.size() == 2) {
			return s.get(0) + ", " + s.get(1);
		}
		return "";
	}

	private List<String> citiesFromDB(String s) {
		List<String> cities = new ArrayList<String>();
		if (!s.isEmpty()) {
			for (String add : s.split(",")) {
				cities.add(add.trim());
			}
		}
		return cities;
	}
}
