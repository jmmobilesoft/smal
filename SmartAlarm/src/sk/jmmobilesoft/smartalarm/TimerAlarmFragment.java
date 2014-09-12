package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Timer;
import sk.jmmobilesoft.smartalarm.model.TimerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TimerAlarmFragment extends Fragment {
	
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
		list.setAdapter(adapter);
		super.onResume();
	}
}
