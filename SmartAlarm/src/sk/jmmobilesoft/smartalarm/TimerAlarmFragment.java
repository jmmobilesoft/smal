package sk.jmmobilesoft.smartalarm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class TimerAlarmFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.timer_alarm_fragment, container,
				false);

		NumberPicker hourPicker = (NumberPicker) view.findViewById(R.id.hours_picker);
		NumberPicker minutePicker = (NumberPicker) view.findViewById(R.id.minutes_picker);

		hourPicker.setMaxValue(100);
		minutePicker.setMaxValue(59);
		return view;
	}

}
