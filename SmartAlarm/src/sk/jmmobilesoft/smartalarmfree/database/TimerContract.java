package sk.jmmobilesoft.smartalarmfree.database;

import android.provider.BaseColumns;

public abstract class TimerContract {

	public TimerContract() {}
	
	public static abstract class TimerModel implements BaseColumns{
		public static final String TABLE_NAME = "timer";
		public static final String COLUMN_NAME_TIMER_NAME = "name";
		public static final String COLUMN_NAME_TIMER_TIME_HOUR = "hour";
		public static final String COLUMN_NAME_TIMER_TIME_MINUTE = "minute";
		public static final String COLUMN_NAME_TIMER_TIME_SECOND = "second";
		public static final String COLUMN_NAME_TIMER_ENABLED = "enabled";
		public static final String COLUMN_NAME_TIMER_TONE = "tone";
		public static final String COLUMN_NAME_TIMER_VOLUME = "volume";
		public static final String COLUMN_NAME_TIMER_START = "start";
	}
	
}
