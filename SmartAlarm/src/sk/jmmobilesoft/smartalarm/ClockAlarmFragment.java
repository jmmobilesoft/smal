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

	private ListView list;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		View view = initView(container);
		refreshClocksList();
		return view;
	}

	@Override
	public void onResume() {
		refreshClocksList();
		super.onResume();
	}

	private View initView(ViewGroup container) {
		View v = getLayoutInflater(getArguments()).inflate(
				R.layout.clock_fragment, container, false);

		list = (ListView) v.findViewById(R.id.clock_listview);
		return v;
	}

	private void refreshClocksList() {
		DBHelper db = new DBHelper(getActivity());
		List<Clock> clockList = null;
		try {
			clockList = db.getClocks();
		} catch (IllegalStateException e) {
			Log.i("INFO", "clock databse is empty");
		}
		if (clockList == null) {
			clockList = new ArrayList<Clock>();
		}
		ClockAdapter adapter = new ClockAdapter(this, clockList,
				this.getArguments());
		list.setAdapter(adapter);
	}
}
