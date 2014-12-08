package sk.jmmobilesoft.smartalarm.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import sk.jmmobilesoft.smartalarm.database.ClockContract.ClockModel;
import sk.jmmobilesoft.smartalarm.model.Clock;

public class ClockDAO {

	private static final String SQL_CREATE_TABLE = "CREATE TABLE "
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
	
	private static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST "
			+ ClockModel.TABLE_NAME;
	
	public void createTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	public void dropTable(SQLiteDatabase db) {
		db.execSQL(SQL_DROP_TABLE);
	}
	
	public long createClock(SQLiteDatabase db, Clock clock) {
		ContentValues values = populateClockContent(clock);
		long id = db.insert(ClockModel.TABLE_NAME, null,
				values);
		db.close();
		return id;
	}
	
	public long updateClock(SQLiteDatabase db, Clock clock) {
		ContentValues values = populateClockContent(clock);
		long id = db.update(ClockModel.TABLE_NAME, values,
				ClockModel._ID + " =?",
				new String[] { String.valueOf(clock.getId()) });
		db.close();
		return id;
	}
	
	public int deleteClock(SQLiteDatabase db, long id) {
		int rows = db.delete(ClockModel.TABLE_NAME,
				ClockModel._ID + " =?", new String[] { String.valueOf(id) });
		db.close();
		return rows;
	}
	
	public Clock getClock(SQLiteDatabase db, long id) {
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
	
	public List<Clock> getClocks(SQLiteDatabase db) {
		String select = "SELECT * FROM " + ClockModel.TABLE_NAME;
		Cursor c = db.rawQuery(select, null);
		List<Clock> clockList = new ArrayList<Clock>();
		while (c.moveToNext()) {
			clockList.add(populateClockModel(c));
		}
		db.close();
		return clockList;
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
