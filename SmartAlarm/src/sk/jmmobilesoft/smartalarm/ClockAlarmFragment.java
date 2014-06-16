package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.ClockAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

public class ClockAlarmFragment extends Fragment {

	private List<Clock> clockList;
	private ClockAdapter adapter;
	private LayoutInflater inflater;
	private ViewGroup container;
	private PopupWindow pw;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		this.container = container;
		final View view = inflater.inflate(R.layout.clock_alarm_fragment,
				container, false);

		clockList = new ArrayList<Clock>();
		ListView list = (ListView) view.findViewById(R.id.listview);
		clockList.add(new Clock("18:18", false));
		clockList.add(new Clock("20:20", true));
		adapter = new ClockAdapter(this, clockList, savedInstanceState);
		list.setAdapter(adapter);

		Button add = (Button) view.findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openPopUp();
			}
		});

		return view;
	}

	private void openPopUp() {
		View view = inflater.inflate(R.layout.clock_popup_window, null);
		pw = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pw.showAtLocation(view, Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL);

		final NumberPicker hours = (NumberPicker) view
				.findViewById(R.id.clock_hours_picker);
		final NumberPicker minutes = (NumberPicker) view
				.findViewById(R.id.clock_minutes_picker);
		minutes.setFormatter(new NumberPicker.Formatter() {
			
			@Override
			public String format(int value) {
				String ret = "";
				if(value < 10){
					ret = "0";
				}
				ret += Integer.toString(value);
				return ret;
			}
		});
		
		hours.setMinValue(0);
		minutes.setMinValue(0);
		hours.setMaxValue(23);
		minutes.setMaxValue(59);

		Button ok = (Button) view.findViewById(R.id.clock_popup_ok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String clock = hours.getValue() + ":";
				if(minutes.getValue() < 10){
					clock += "0" + minutes.getValue();
				}
				else{
					clock += minutes.getValue();
				}
				Clock addClock = new Clock(clock , true);
				adapter.addItem(v, addClock);
				adapter.notifyDataSetChanged();
				pw.dismiss();
			}
		});

		Button cancel = (Button) view.findViewById(R.id.clock_popup_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}
		});
	}
}
