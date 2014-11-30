package sk.jmmobilesoft.smartalarm.model;

import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarm.TimerViewActivity;
import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.service.Helper;
import sk.jmmobilesoft.smartalarm.service.TimerSetting;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TimerAdapter extends BaseAdapter {

	private List<Timer> timers;
	private DBHelper db;
	private Fragment context;
	private Bundle savedInstanceState;

	public TimerAdapter(Fragment context, List<Timer> timers, Bundle state) {
		super();
		this.context = context;
		this.timers = timers;
		this.savedInstanceState = state;
		db = new DBHelper(context.getActivity());
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
				.getLayoutInflater(savedInstanceState);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.timer_adapter_item, null);
		}
		Timer timer = timers.get(position);
		System.out.println(timer);
		if (timer != null) {
			TextView time = (TextView) convertView
					.findViewById(R.id.timer_item_time);
			time.setText(Helper.format(timer.getHours()) + ":"
					+ Helper.format(timer.getMinutes()) + ":"
					+ Helper.format(timer.getSeconds()));
			TextView name = (TextView) convertView
					.findViewById(R.id.timer_item_name);
			name.setText(timer.getName());
			final CheckBox active = (CheckBox) convertView
					.findViewById(R.id.timer_item_active);
			active.setChecked(timer.isActive());
			active.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Timer timer = (Timer) getItem(position);
					timer.setActive(active.isChecked());
					db.updateTimer(timer);
					TimerSetting.setTimer(context.getActivity(), timer.getId());
				}
			});
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context.getActivity(),
						TimerViewActivity.class);
				intent.putExtra("id", getItemId(position));
				context.startActivity(intent);
			}
		});
		return convertView;
	}

}
