package sk.jmmobilesoft.smartalarmfree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarmfree.database.DBHelper;
import sk.jmmobilesoft.smartalarmfree.model.Timer;
import sk.jmmobilesoft.smartalarmfree.model.TimerAdapter;
import sk.jmmobilesoft.smartalarmfree.model.TimerComparator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TimerFragment extends Fragment {
	
	private List<Timer> timerList;
	private TimerAdapter adapter;
	private DBHelper db;
	private Bundle bundle;
	private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		bundle = savedInstanceState;
		final View view = inflater.inflate(R.layout.timer_fragment,
				container, false);
		db = new DBHelper(getActivity());
		list = (ListView) view.findViewById(R.id.timer_listview);
		try{
			timerList = db.getTimers();
		}catch(NullPointerException e){
			Log.i("INFO", "timer databse is empty");
		}
		if(timerList == null){
			timerList = new ArrayList<Timer>();
		}
		adapter = new TimerAdapter(this, timerList, savedInstanceState);
		Collections.sort(timerList, new TimerComparator());
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		return view;
	}
	
	@Override
	public void onResume() {
		timerList = db.getTimers();
		if (timerList == null) {
			timerList = new ArrayList<Timer>();
		}
		adapter = new TimerAdapter(this, timerList, bundle);
		Collections.sort(timerList, new TimerComparator());
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		super.onResume();
	}
}
