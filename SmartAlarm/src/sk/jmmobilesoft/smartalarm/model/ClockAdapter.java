package sk.jmmobilesoft.smartalarm.model;

import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ClockAdapter extends BaseAdapter{

	List<Clock> clocks;
	Fragment context;
	Bundle savedInstanceState;
	
	public ClockAdapter(Fragment context, List<Clock> clocks, Bundle state){
		super();
		this.context = context;
		this.clocks = clocks;
		this.savedInstanceState = state;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = context.getLayoutInflater(savedInstanceState);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.clock_item_fragment, null);
		}
		
		Clock clock = clocks.get(position);
		TextView clockText = (TextView) convertView.findViewById(R.id.clock);
		clockText.setText(clock.getTime());
		CheckBox active = (CheckBox) convertView.findViewById(R.id.clockbutton);
		active.setChecked(clock.isActive());
		return convertView;
	}

	public View addItem(View convertView, Clock newClock){
		clocks.add(newClock);
		return convertView;
	}
}
