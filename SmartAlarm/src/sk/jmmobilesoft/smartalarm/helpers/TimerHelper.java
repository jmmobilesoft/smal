package sk.jmmobilesoft.smartalarm.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.widget.TextView;
import sk.jmmobilesoft.smartalarm.model.Timer;

public class TimerHelper {

	public static void setTimerAdapterLabels(Timer timer, boolean active,
			TextView start, TextView end) {
		if (active) {
			SimpleDateFormat formater = new SimpleDateFormat("hh:mm:ss");
			start.setText(formater.format(timer.getStart().getTime()));
			Calendar c = timer.getStart();
			c.add(Calendar.HOUR_OF_DAY, timer.getHours());
			c.add(Calendar.MINUTE, timer.getMinutes());
			c.add(Calendar.SECOND, timer.getSeconds());
			end.setText(formater.format(c.getTime()));
		} else {
			start.setText("-");
			end.setText("-");
		}
	}

}
