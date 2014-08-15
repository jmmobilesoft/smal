package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Timer;
import sk.jmmobilesoft.smartalarm.service.Helper;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class TimerViewActivity extends Activity {
	
	private DBHelper db = new DBHelper(this);
	
	private long id;
	private EditText name;
	private NumberPicker hours;
	private NumberPicker minutes;
	private Timer t;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("INFO", "TimerViewActivity.class: started");
		setContentView(R.layout.timer_view_activity);
		id = getIntent().getExtras().getLong("id");
		Log.i("INFO", "Timer id:" + id);
		initComponents();
		minutes.setFormatter(Helper.getNumberPickFormater());
		hours.setFormatter(Helper.getNumberPickFormater());
		super.onCreate(savedInstanceState);
	}
	
	private void initComponents(){
		name = (EditText) findViewById(R.id.timer_view_activity_name);
		hours = (NumberPicker) findViewById(R.id.timer_view_activity_hours_picker);
		minutes = (NumberPicker) findViewById(R.id.timer_view_activity_minutes_picker);
		System.out.println(hours == null);
		hours.setMaxValue(72);
		hours.setMinValue(0);
		minutes.setMaxValue(59);
		minutes.setMinValue(0);
		if(id != 0){
			t = db.getTimer(id);
			name.setText(t.getName());
			hours.setValue(t.getHours());
			minutes.setValue(t.getMinutes());
		}
		Button save = (Button) findViewById(R.id.timer_view_activity_save);
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(t == null){
					t = new Timer();
				}
				t.setName(name.getText().toString());
				t.setHours(hours.getValue());
				t.setMinutes(minutes.getValue());
				t.setActive(true);
				if(t.getId() == -1){
					t.setId(db.createTimer(t));
				}
				else{
					db.updateTimer(t);
				}
				setResult(10);
				Log.i("INFO", "TimerViewActivity.class: finishing - save");
				finish();
			}
		});
		Button delete = (Button) findViewById(R.id.timer_view_activity_cancel);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (t != null) {
					db.deleteClock(t.getId());
				}
				setResult(10);
				Log.i("INFO", "TimerViewActivity.class: finishing - delete");
				finish();
			}
		});
	}

}
