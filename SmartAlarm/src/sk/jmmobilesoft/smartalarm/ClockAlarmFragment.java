package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.ClockAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ClockAlarmFragment extends Fragment {

	private List<Clock> clockList;
	private ClockAdapter adapter;
	private DBHelper db;
	private Bundle bundle;
	private ListView list;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		bundle = savedInstanceState;
		final View view = inflater.inflate(R.layout.clock_fragment, container,
				false);
		db = new DBHelper(getActivity());
		list = (ListView) view.findViewById(R.id.clock_listview);
		try {
			clockList = db.getClocks();
		} catch (IllegalStateException e) {
			Log.i("INFO", "clock databse is empty");
		}
		if (clockList == null) {
			clockList = new ArrayList<Clock>();
		}
		adapter = new ClockAdapter(this, clockList, savedInstanceState);
		list.setAdapter(adapter);
		return view;
	}

	@Override
	public void onResume() {
		clockList = db.getClocks();
		if (clockList == null) {
			clockList = new ArrayList<Clock>();
		}
		adapter = new ClockAdapter(this, clockList, bundle);
		list.setAdapter(adapter);
		super.onResume();
	}
}
