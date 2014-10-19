package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.ClockRemoveAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class ClockRemoveActivity extends Activity {

	private DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.clock_remove_activity);
		db = new DBHelper(this);
		ListView list = (ListView) findViewById(R.id.clock_remove_listview);
		List<Clock> clockList = null;
		try {
			clockList = db.getClocks();
		} catch (IllegalStateException e) {
			Log.i("INFO", "clock databse is empty");
		}
		if (clockList == null) {
			clockList = new ArrayList<Clock>();
		}
		ClockRemoveAdapter adapter = new ClockRemoveAdapter(this, clockList, savedInstanceState);
		list.setAdapter(adapter);
		initComponents(adapter);
		super.onCreate(savedInstanceState);
	}
	
	private void initComponents(final ClockRemoveAdapter adapter){
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
				for(int i = 0; i< adapter.getCount(); i++){
					if(ClockRemoveAdapter.checkboxes[i]){
						db.deleteClock(adapter.getItemId(i));
						//TODO log
					}
				}
				finish();
			}
		});
	}
	
}
