package sk.jmmobilesoft.smartalarmfree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarmfree.database.DBHelper;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import sk.jmmobilesoft.smartalarmfree.model.Timer;
import sk.jmmobilesoft.smartalarmfree.model.TimerComparator;
import sk.jmmobilesoft.smartalarmfree.model.TimerRemoveAdapter;
import sk.jmmobilesoft.smartalarmfree.service.TimerSetting;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TimerRemoveActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("TimerRemoveActivity: started");
		setContentView(R.layout.timer_remove_activity);
		initView();
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		DBHelper db = new DBHelper(this);
		List<Timer> timerList = null;
		try {
			timerList = db.getTimers();
		} catch (IllegalStateException e) {
			Log.i("INFO", "timer databse is empty");
		}
		if (timerList == null) {
			timerList = new ArrayList<Timer>();
		}
		TimerRemoveAdapter adapter = new TimerRemoveAdapter(this, timerList,
				getIntent().getExtras());
		ListView list = (ListView) findViewById(R.id.timer_remove_listview);
		Collections.sort(timerList, new TimerComparator());
		adapter.notifyDataSetChanged();
		list.setAdapter(adapter);
		initButtons(adapter, db);
	}

	private void initButtons(final TimerRemoveAdapter adapter, final DBHelper db) {
		Button cancel = (Button) findViewById(R.id.timer_remove_activity_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button delete = (Button) findViewById(R.id.timer_remove_activity_delete);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < adapter.getCount(); i++) {
					if (TimerRemoveAdapter.checkboxes[i]) {
						TimerSetting.deactivateTimer(
								db.getTimer(adapter.getItemId(i)),
								getApplicationContext());
						db.deleteTimer(adapter.getItemId(i));
					}
				}
				finish();
			}
		});
	}

}
