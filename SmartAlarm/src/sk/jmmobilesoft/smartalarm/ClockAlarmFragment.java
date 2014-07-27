package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.ClockDBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.ClockAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ClockAlarmFragment extends Fragment {

	private List<Clock> clockList;
	private ClockAdapter adapter;
	private ClockDBHelper db;
	private Bundle bundle;
	private ListView list;
	
	
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		bundle = savedInstanceState;
		final View view = inflater.inflate(R.layout.clock_alarm_fragment,
				container, false);
		db = new ClockDBHelper(getActivity());
		list = (ListView) view.findViewById(R.id.listview);
		clockList = db.getClocks();
		if (clockList == null) {
			clockList = new ArrayList<Clock>();
		}
		adapter = new ClockAdapter(this, clockList, savedInstanceState);
		list.setAdapter(adapter);

		Button add = (Button) view.findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentA = new Intent(getActivity(),
						ClockViewActivity.class);
				intentA.putExtra("id", 0);
				startActivityForResult(intentA, 10);
			}
		});

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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 10) {
			clockList = db.getClocks();
			if (clockList == null) {
				clockList = new ArrayList<Clock>();
			}
			adapter = new ClockAdapter(this, clockList, bundle);
			list.setAdapter(adapter);
		}
	}
}
