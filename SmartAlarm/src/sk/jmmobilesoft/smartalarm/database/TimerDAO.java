package sk.jmmobilesoft.smartalarm.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import sk.jmmobilesoft.smartalarm.database.TimerContract.TimerModel;
import sk.jmmobilesoft.smartalarm.model.Timer;

public class TimerDAO {

	private static final String SQL_CREATE_TABLE = "CREATE TABLE "
			+ TimerModel.TABLE_NAME + " (" + TimerModel._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ TimerModel.COLUMN_NAME_TIMER_NAME + " TEXT,"
			+ TimerModel.COLUMN_NAME_TIMER_TIME_HOUR + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_TIME_MINUTE + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_TIME_SECOND + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_ENABLED + " INTEGER,"
			+ TimerModel.COLUMN_NAME_TIMER_TONE + " TEXT,"
			+ TimerModel.COLUMN_NAME_TIMER_VOLUME + " TEXT" + ")";
	
	private static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST "
			+ TimerModel.TABLE_NAME;

	public void createTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE);
	}

	public void dropTable(SQLiteDatabase db) {
		db.execSQL(SQL_DROP_TABLE);
	}
	
	public long createTimer(SQLiteDatabase db, Timer timer) {
		ContentValues values = populateTimerContent(timer);
		long id = db.insert(TimerModel.TABLE_NAME, null,
				values);
		db.close();
		return id;
	}
	
	public long updateTimer(SQLiteDatabase db, Timer timer) {
		ContentValues values = populateTimerContent(timer);
		long id = db.update(TimerModel.TABLE_NAME, values,
				TimerModel._ID + "=?",
				new String[] { String.valueOf(timer.getId()) });
		db.close();
		return id;
	}
	
	public int deleteTimer(SQLiteDatabase db, long id) {
		int rows = db.delete(TimerModel.TABLE_NAME,
				TimerModel._ID + " =?", new String[] { String.valueOf(id) });
		db.close();
		return rows;
	}
	
	public Timer getTimer(SQLiteDatabase db, long id) {
		String select = "SELECT * FROM " + TimerModel.TABLE_NAME + " WHERE "
				+ TimerModel._ID + " = " + id;

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			db.close(); // TODO think about
			return populateTimerModel(c);
		}
		return null;
	}
	
	public List<Timer> getTimers(SQLiteDatabase db) {
		String select = "SELECT * FROM " + TimerModel.TABLE_NAME;
		Cursor c = db.rawQuery(select, null);
		List<Timer> timerList = new ArrayList<Timer>();
		while (c.moveToNext()) {
			timerList.add(populateTimerModel(c));
		}
		db.close();
		return timerList;
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
