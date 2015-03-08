package sk.jmmobilesoft.smartalarmfree.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarmfree.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarmfree.helpers.Helper;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ClockRemoveAdapter extends ArrayAdapter<Clock> {

	private List<Clock> clocks;
	private Activity context;
	private static boolean[] checkboxes;

	public ClockRemoveAdapter(Activity context, List<Clock> clocks, Bundle state) {
		super(context, 0, clocks);
		this.context = context;
		this.clocks = clocks;
		checkboxes = new boolean[getCount()];
	}

	public boolean[] getCheckboxes(){
		return checkboxes;
	}
	
	@Override
	public int getCount() {
		return clocks.size();
	}

	@Override
	public Clock getItem(int position) {
		return clocks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return clocks.get(position).getId();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = context.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.clock_remove_adapter_item,
					null);
		}
		setView(convertView, position);
		return convertView;
	}

	public void setMyColor(List<TextView> list, Clock clock) {
		for (int i = 0; i <= 6; i++) {
			TextView text = list.get(i);
			if (clock.getRepeat()[i] == 0) {
				text.setTextColor(Color.rgb(112, 112, 112));
			}
			if (clock.getRepeat()[i] == 1) {
				text.setTextColor(Color.rgb(51, 181, 229));
			}
		}
	}

	private void setView(View convertView, final int position) {
		Clock clock = clocks.get(position);
		// TODO ERROR
		TextView clockText = (TextView) convertView
				.findViewById(R.id.clock_remove_item_time);
		clockText.setText(Helper.format(clock.getHour()) + ":"
				+ Helper.format(clock.getMinutes()));
		final CheckBox delete = (CheckBox) convertView
				.findViewById(R.id.clock_remove_item_check);
		TextView name = (TextView) convertView
				.findViewById(R.id.clock_remove_item_name);
		List<TextView> daysList = new ArrayList<>();
		TextView MO = (TextView) convertView
				.findViewById(R.id.clock_remove_item_MO);
		daysList.add(MO);
		TextView TU = (TextView) convertView
				.findViewById(R.id.clock_remove_item_TU);
		daysList.add(TU);
		TextView WE = (TextView) convertView
				.findViewById(R.id.clock_remove_item_WE);
		daysList.add(WE);
		TextView TH = (TextView) convertView
				.findViewById(R.id.clock_remove_item_TH);
		daysList.add(TH);
		TextView FR = (TextView) convertView
				.findViewById(R.id.clock_remove_item_FR);
		daysList.add(FR);
		TextView SA = (TextView) convertView
				.findViewById(R.id.clock_remove_item_SA);
		daysList.add(SA);
		TextView SU = (TextView) convertView
				.findViewById(R.id.clock_remove_item_SU);
		daysList.add(SU);
		setMyColor(daysList, clock);
		name.setText(clock.getName());
		GlobalHelper.setCheckboxStyle(delete);
		delete.setChecked(checkboxes[position]);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				delete.setChecked(!delete.isChecked());
				checkboxes[position] = delete.isChecked();
				System.out.println("checks: " + Arrays.toString(checkboxes));
			}
		});
	}

}
