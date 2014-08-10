package sk.jmmobilesoft.smartalarm.database;

import android.provider.BaseColumns;

public abstract class ClockContract {
	
	public ClockContract() {}
	
	public static abstract class ClockModel implements BaseColumns {
		public static final String TABLE_NAME = "clock";
		public static final String COLUMN_NAME_CLOCK_NAME = "name";
		public static final String COLUMN_NAME_CLOCK_TIME_HOUR = "hour";
		public static final String COLUMN_NAME_CLOCK_TIME_MINUTE = "minute";
		public static final String COLUMN_NAME_CLOCK_REPEAT = "days";
		public static final String COLUMN_NAME_CLOCK_TONE = "tone";
		public static final String COLUMN_NAME_CLOCK_VOLUME = "volume";
		public static final String COLUMN_NAME_CLOCK_ENABLED = "enabled";
	}

}
