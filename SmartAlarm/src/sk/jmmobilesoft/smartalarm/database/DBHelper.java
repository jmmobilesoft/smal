package sk.jmmobilesoft.smartalarm.database;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.ClockContract.ClockModel;
import sk.jmmobilesoft.smartalarm.database.TimerContract.TimerModel;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.Timer;
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
			+ ClockModel.COLUMN_NAME_CLOCK_REPEAT + " TEXT,"
			+ ClockModel.COLUMN_NAME_CLOCK_TONE + " TEXT,"
			+ ClockModel.COLUMN_NAME_CLOCK_VOLUME + " TEXT,"
			+ ClockModel.COLUMN_NAME_CLOCK_ENABLED + " INTEGER" + " )";

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

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_CLOCK);
		db.execSQL(SQL_CREATE_TIMER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_CLOCK);
		db.execSQL(SQL_DROP_TIMER);
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
		return clock;
	}

	private ContentValues populateClockContent(Clock clock) {
		ContentValues values = new ContentValues();
		values.put(ClockModel.COLUMN_NAME_CLOCK_NAME, clock.getName());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TIME_HOUR, clock.getHour());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TIME_MINUTE, clock.getMinutes());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TONE,
				clock.getSound() != null ? clock.getSound().toString() : "");
		values.put(ClockModel.COLUMN_NAME_CLOCK_VOLUME, clock.getVolume());
		values.put(ClockModel.COLUMN_NAME_CLOCK_ENABLED, clock.isActive() ? 1
				: 0);
		values.put(ClockModel.COLUMN_NAME_CLOCK_REPEAT,
				clock.toDBRepeat(clock.getRepeat()));
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
}
