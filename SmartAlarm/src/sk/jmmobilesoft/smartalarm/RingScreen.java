package sk.jmmobilesoft.smartalarm;

import java.util.Arrays;

import sk.jmmobilesoft.smartalarm.database.ClockDBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RingScreen extends Activity {

	private long id;
	
	private ClockDBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		db = new ClockDBHelper(this);
		setContentView(R.layout.ring_screen);
		id = getIntent().getLongExtra("ID", -1);
		System.out.println(db.getClock(id).getRepeat().toString());
		Button dismiss = (Button) findViewById(R.id.ring_dismiss);
		dismiss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Clock c = db.getClock(id);
				if(Arrays.equals(c.getRepeat(), new int[]{0,0,0,0,0,0,0})){
					c.setActive(false);
					db.updateClock(c);
				}
				finish();
			}
		});
		super.onCreate(savedInstanceState);
	}
}
