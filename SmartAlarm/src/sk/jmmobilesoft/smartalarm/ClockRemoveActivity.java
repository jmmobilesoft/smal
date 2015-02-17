package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.ClockHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.ClockComparator;
import sk.jmmobilesoft.smartalarm.model.ClockRemoveAdapter;
import sk.jmmobilesoft.smartalarm.service.ClockSetting;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ClockRemoveActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("ClockRemoveActivity: started");
		setContentView(R.layout.clock_remove_activity);
		initView();
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		DBHelper db = new DBHelper(this);
		List<Clock> clockList = null;
		try {
			clockList = db.getClocks();
		} catch (IllegalStateException e) {
			Log.i("INFO", "clock databse is empty");
		}
		if (clockList == null) {
			clockList = new ArrayList<Clock>();
		}
		ClockRemoveAdapter adapter = new ClockRemoveAdapter(this, clockList,
				getIntent().getExtras());
		ListView list = (ListView) findViewById(R.id.clock_remove_listview);
		Collections.sort(clockList, new ClockComparator());
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		initButtons(adapter, db);
	}

	private void initButtons(final ClockRemoveAdapter adapter, final DBHelper db) {
		Button cancel = (Button) findViewById(R.id.clock_remove_activity_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button delete = (Button) findViewById(R.id.clock_remove_activity_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(Arrays.toString(adapter.getCheckboxes()));
				for (int i = 0; i < adapter.getCount(); i++) {
					if (adapter.getCheckboxes()[i]) {
						ClockSetting.deactivateClock(
								db.getClock(adapter.getItemId(i)),
								getApplicationContext());
						db.deleteClock(adapter.getItemId(i));
						ClockHelper.determineAlarmIcon(getApplicationContext());
					}
				}
				finish();
			}
		});
	}

}
