package sk.jmmobilesoft.smartalarm.model;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.ClockViewActivity;
import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarm.database.ClockDBHelper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


public class ClockAdapter extends BaseAdapter {

	
	List<Clock> clocks;
	Fragment context;
	Bundle savedInstanceState;
	private ClockDBHelper db;

	public ClockAdapter(Fragment context, List<Clock> clocks, Bundle state) {
		super();
		this.context = context;
		this.clocks = clocks;
		this.savedInstanceState = state;
		db = new ClockDBHelper(context.getActivity());
	}

	@Override
	public int getCount() {
		return clocks.size();
	}

	@Override
	public Object getItem(int position) {
		return clocks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return clocks.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater(savedInstanceState);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.clock_item_fragment, null);
		}

		Clock clock = clocks.get(position);
			
		TextView clockText = (TextView) convertView.findViewById(R.id.clock_item_time);
		clockText.setText(format(clock.getHour()) + ":" + format(clock.getMinutes()));
		CheckBox active = (CheckBox) convertView.findViewById(R.id.clock_item_active);
		TextView name = (TextView) convertView.findViewById(R.id.clock_item_name);
		List<TextView> daysList = new ArrayList<>();
		TextView MO = (TextView) convertView.findViewById(R.id.clock_item_MO);
		daysList.add(MO);
		TextView TU = (TextView) convertView.findViewById(R.id.clock_item_TU);
		daysList.add(TU);
		TextView WE = (TextView) convertView.findViewById(R.id.clock_item_WE);
		daysList.add(WE);
		TextView TH = (TextView) convertView.findViewById(R.id.clock_item_TH);
		daysList.add(TH);
		TextView FR = (TextView) convertView.findViewById(R.id.clock_item_FR);
		daysList.add(FR);
		TextView SA = (TextView) convertView.findViewById(R.id.clock_item_SA);
		daysList.add(SA);
		TextView SU = (TextView) convertView.findViewById(R.id.clock_item_SU);
		daysList.add(SU);
		setMyColor(daysList, clock);
 		name.setText(clock.getName());
		active.setChecked(clock.isActive());
		active.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Clock clock = (Clock) getItem(position);
				clock.setActive(isChecked);
				db.updateClock(clock);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentA = new Intent(context.getActivity(),
						ClockViewActivity.class);
				intentA.putExtra("id", getItemId(position));
				context.startActivity(intentA);
				
			}
		});		
		return convertView;
	}
	
	public View addItem(View convertView, Clock newClock) {
		clocks.add(newClock);
		return convertView;
	}
	
	public void setMyColor(List<TextView> list, Clock clock){
		for(int i = 0; i <= 6; i++){
			TextView text = list.get(i);
			if(clock.getRepeat()[i] == 0){
				text.setTextColor(Color.RED);
			}
			if(clock.getRepeat()[i] == 1){
				text.setTextColor(Color.rgb(34, 139, 34));
			}
		}
	}
	
	public String format(int value) {
		String ret = "";
		if (value < 10) {
			ret = "0";
		}
		ret += Integer.toString(value);
		return ret;
	}
}
