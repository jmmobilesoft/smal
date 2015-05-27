package sk.jmmobilesoft.smartalarm.model;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.helpers.TimerHelper;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TimerRemoveAdapter extends BaseAdapter {
	
	private List<Timer> timers;
	private Activity context;
	public static boolean[] checkboxes;

	public TimerRemoveAdapter(Activity context, List<Timer> timers, Bundle state) {
		super();
		this.context = context;
		this.timers = timers;
		checkboxes = new boolean[getCount()];
	}

	@Override
	public int getCount() {
		return timers.size();
	}

	@Override
	public Object getItem(int position) {
		return timers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return timers.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = context
				.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.timer_remove_adapter_item, null);
		}
		setView(convertView, position);
		return convertView;
	}
	
	private void setView(View convertView,final int position){
		Timer timer = timers.get(position);

		
		final CheckBox delete = (CheckBox) convertView
				.findViewById(R.id.timer_remove_item_delete);
		TextView name = (TextView) convertView
				.findViewById(R.id.timer_remove_item_name);
		name.setText(timer.getName());
		TextView time = (TextView) convertView
				.findViewById(R.id.timer_remove_item_time);
		time.setText(Helper.format(timer.getHours()) + ":"
				+ Helper.format(timer.getMinutes()) + ":"
				+ Helper.format(timer.getSeconds()));
		final TextView start = (TextView) convertView.findViewById(R.id.timer_remove_item_start);
		final TextView end = (TextView) convertView.findViewById(R.id.timer_remove_item_end);
		
		TimerHelper.setTimerAdapterLabels(timer, timer.isActive(), start, end);
		GlobalHelper.setCheckboxStyle(delete);
		delete.setChecked(checkboxes[position]);
		delete.setClickable(false);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delete.setChecked(!delete.isChecked());
				checkboxes[position] = delete.isChecked();
			}
		});
	}

}
