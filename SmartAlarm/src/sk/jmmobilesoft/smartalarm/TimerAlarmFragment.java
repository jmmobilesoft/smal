package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Timer;
import sk.jmmobilesoft.smartalarm.model.TimerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
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
		
		Button add = (Button) view.findViewById(R.id.timer_add);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						TimerViewActivity.class);
				intent.putExtra("id", 0);
				startActivityForResult(intent, 10);
			}
		});
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 10) {
			timerList = db.getTimers();
			if (timerList == null) {
				timerList = new ArrayList<Timer>();
			}
			adapter = new TimerAdapter(this, timerList, bundle);
			list.setAdapter(adapter);
		}
	}
}
