package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.ClockAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ClockAlarmFragment extends Fragment {

	private List<Clock> clockList;
	private ClockAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.clock_alarm_fragment, container,
				false);

		clockList = new ArrayList<Clock>();
		ListView list =  (ListView) view.findViewById(R.id.listview);
		clockList.add(new Clock("18:18", false));
		clockList.add(new Clock("20:20", true));
		adapter = new ClockAdapter(this, clockList, savedInstanceState);
		list.setAdapter(adapter);
		
		Button add = (Button) view.findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				adapter.addItem(v, new Clock("10:15", true));
				adapter.notifyDataSetChanged();
			}
		});
		
		return view;
	}
}
