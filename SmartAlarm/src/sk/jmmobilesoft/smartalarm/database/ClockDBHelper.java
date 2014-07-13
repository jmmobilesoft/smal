package sk.jmmobilesoft.smartalarm.database;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.ClockContract.ClockModel;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class ClockDBHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "smartalarm.db";
	
	public ClockDBHelper(Context context){
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static final String SQL_DELETE_CLOCK = 
			"DROP TABLE IF EXIST " + ClockModel.TABLE_NAME;
	
	private static final String SQL_CREATE_CLOCK = "CREATE TABLE " + ClockModel.TABLE_NAME + " (" +
			ClockModel._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			ClockModel.COLUMN_NAME_CLOCK_NAME + " TEXT," +
			ClockModel.COLUMN_NAME_CLOCK_TIME_HOUR + " INTEGER," +
			ClockModel.COLUMN_NAME_CLOCK_TIME_MINUTE + " INTEGER," +
			ClockModel.COLUMN_NAME_CLOCK_REPEAT + " TEXT," +
			ClockModel.COLUMN_NAME_CLOCK_TONE + " TEXT," +
			ClockModel.COLUMN_NAME_CLOCK_ENABLED + " INTEGER" + " )";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_CLOCK);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_CLOCK);
		onCreate(db);
	}

	private Clock populateModel(Cursor c){
		Clock clock = new Clock();
		clock.setId(c.getLong(c.getColumnIndex(ClockModel._ID)));
		clock.setName(c.getString(c.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_NAME)));
		clock.setHour(c.getInt(c.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TIME_HOUR)));
		clock.setMinutes(c.getInt(c.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TIME_MINUTE)));
		clock.setActive(c.getInt(c.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_ENABLED)) == 1? true : false);
		clock.setSound(c.getString(c.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TONE)) != "" ? Uri.parse(c.getString(c.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_TONE))) : null);
		clock.setRepeat(clock.fromDBRepeat(c.getString(c.getColumnIndex(ClockModel.COLUMN_NAME_CLOCK_REPEAT))));
		return clock;
	}
	
	private ContentValues populateContent(Clock clock){
		ContentValues values = new ContentValues();
		values.put(ClockModel.COLUMN_NAME_CLOCK_NAME, clock.getName());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TIME_HOUR, clock.getHour());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TIME_MINUTE, clock.getMinutes());
		values.put(ClockModel.COLUMN_NAME_CLOCK_TONE, clock.getSound() != null? clock.getSound().toString() : "");
		values.put(ClockModel.COLUMN_NAME_CLOCK_ENABLED, clock.isActive()? 1 : 0);
		values.put(ClockModel.COLUMN_NAME_CLOCK_REPEAT, clock.toDBRepeat(clock.getRepeat()));
		return values;
	}
	
	public long createClock(Clock clock){
		ContentValues values = populateContent(clock);
		return getWritableDatabase().insert(ClockModel.TABLE_NAME, null, values);
	}
	
	public long updateClock(Clock clock){
		ContentValues values = populateContent(clock);
		return getWritableDatabase().update(ClockModel.TABLE_NAME, values, ClockModel._ID + " =?", new String[]{ String.valueOf(clock.getId())});
	}
	
	public Clock getClock(long id){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String select = "SELECT * FROM " + ClockModel.TABLE_NAME + " WHERE " + ClockModel._ID + " = " + id;
		
		Cursor c = db.rawQuery(select, null);
		
		if(c.moveToNext()){
			return populateModel(c);
		}
		return null;
 	}
	
	public List<Clock> getClocks(){
		SQLiteDatabase db = this.getReadableDatabase();
		
		String select = "SELECT * FROM " + ClockModel.TABLE_NAME;
		
		Cursor c = db.rawQuery(select, null);
		
		List<Clock> clockList = new ArrayList<Clock>();
		
		while(c.moveToNext()) {
			clockList.add(populateModel(c));
		}
		
		if(!clockList.isEmpty()){
			return clockList;
		}
		return null;
	}
	
	public int deleteClock(long id){
		return getWritableDatabase().delete(ClockModel.TABLE_NAME, ClockModel._ID + " =?", new String[]{ String.valueOf(id)});
	}
}
