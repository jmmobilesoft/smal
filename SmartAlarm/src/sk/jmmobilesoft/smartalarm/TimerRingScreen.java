package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Timer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TimerRingScreen extends Activity{
	
	private DBHelper db;
	
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i("INFO", "TimerRingScreen activity started");
		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED // START
																			// DISPLAY
																			// etc
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		db = new DBHelper(this);
		setContentView(R.layout.timer_ring_activity);
		id = getIntent().getLongExtra("ID", -1);
		final Timer t = db.getTimer(id);
		TextView name = (TextView) findViewById(R.id.timer_ring_activity_Name);
		if(name.equals("")){
			name.setText("No specified name timer");
		}else {
			name.setText(t.getName());
		}
		
		Button ok = (Button) findViewById(R.id.timer_ring_activity_OK);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();			
			}
		});
		
		super.onCreate(savedInstanceState);
	}

}
