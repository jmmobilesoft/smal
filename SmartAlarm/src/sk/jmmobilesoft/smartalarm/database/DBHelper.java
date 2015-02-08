package sk.jmmobilesoft.smartalarm.database;

import java.util.List;

import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.Timer;
import sk.jmmobilesoft.smartalarm.model.Weather;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "smartalarm.db";

	private WeatherDAO weatherDAO;
	private WeatherForecastDAO weatherForecastDAO;
	private ClockDAO clockDAO;
	private TimerDAO timerDAO;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		weatherDAO = new WeatherDAO();
		weatherForecastDAO = new WeatherForecastDAO();
		clockDAO = new ClockDAO();
		timerDAO = new TimerDAO();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		weatherDAO.createTable(db);
		weatherForecastDAO.createTable(db);
		clockDAO.createTable(db);
		timerDAO.createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		weatherDAO.dropTable(db);
		weatherForecastDAO.createTable(db);
		clockDAO.createTable(db);
		timerDAO.dropTable(db);
		onCreate(db);
	}

	public long createClock(Clock clock) {
		return clockDAO.createClock(getWritableDatabase(), clock);
	}

	public long updateClock(Clock clock) {
		return clockDAO.updateClock(getWritableDatabase(), clock);
	}
	
	public int deleteClock(long id) {
		return clockDAO.deleteClock(getWritableDatabase(), id);
	}

	public Clock getClock(long id) {
		return clockDAO.getClock(getReadableDatabase(), id);
	}
	
	public Clock getClockByTime(int hour, int minutes){
		return clockDAO.getClockByTime(getReadableDatabase(), hour, minutes);
	}

	public List<Clock> getClocks() {
		return clockDAO.getClocks(getReadableDatabase());
	}

	public long createTimer(Timer timer) {
		return timerDAO.createTimer(getWritableDatabase(), timer);
	}

	public long updateTimer(Timer timer) {
		return timerDAO.updateTimer(getWritableDatabase(), timer);
	}

	public int deleteTimer(long id) {
		return timerDAO.deleteTimer(getWritableDatabase(), id);
	}
	
	public Timer getTimer(long id) {
		return timerDAO.getTimer(getReadableDatabase(), id);
	}

	public List<Timer> getTimers() {
		return timerDAO.getTimers(getReadableDatabase());
	}	

	public long createWeatherForecast(WeatherForecast weather) {
		return weatherForecastDAO.createWeatherForecast(getWritableDatabase(),
				weather);
	}

	public long updateWeatherForecast(WeatherForecast weather) {
		return weatherForecastDAO.updateWeatherForecast(getWritableDatabase(),
				weather);
	}

	public int deleteWeatherForecast(long id) {
		return weatherForecastDAO.deleteWeatherForecast(getWritableDatabase(),
				id);
	}

	public WeatherForecast getWeatherForecast(long id) {
		return weatherForecastDAO.getWeatherForecast(getReadableDatabase(), id);
	}

	public WeatherForecast getWeatherForecastByCity(String city) {
		return weatherForecastDAO.getWeatherForecastByCity(
				getReadableDatabase(), city);
	}

	public List<WeatherForecast> getWeatherForecast() {
		return weatherForecastDAO.getWeatherForecast(getReadableDatabase());
	}

	public long createWeather(Weather weather) {
		return weatherDAO.createWeather(getWritableDatabase(), weather);
	}

	public long updateWeather(Weather weather) {
		return weatherDAO.updateWeather(getWritableDatabase(), weather);
	}

	public int deleteWeather(long id) {
		return weatherDAO.deleteWeather(getWritableDatabase(), id);
	}

	public void deleteAllWeather() {
		weatherDAO.deleteAll(getWritableDatabase());
	}
	
	public void deleteWeatherByCity(String city){
		weatherDAO.deleteWeatherByCity(getWritableDatabase(), city);
	}

	public Weather getWeather(long id) {
		return weatherDAO.getWeather(getReadableDatabase(), id);
	}

	public List<Weather> getWeather() {
		return weatherDAO.getWeather(getReadableDatabase());
	}

	//TODO id namiesto city
	public Weather getWeatherByCityDate(String city, String date) {
		return weatherDAO.getWeatherByCityDate(getReadableDatabase(), city,
				date);
	}
}
