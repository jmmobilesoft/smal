package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.ClockDBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.service.ClockService;
import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ClockViewActivity extends Activity {

	ClockDBHelper db = new ClockDBHelper(this);

	private EditText name;
	private NumberPicker hours;
	private NumberPicker minutes;
	private ToggleButton MO;
	private ToggleButton TU;
	private ToggleButton WE;
	private ToggleButton TH;
	private ToggleButton FR;
	private ToggleButton SA;
	private ToggleButton SU;
	private Uri sound;
	private Button save;
	private Button delete;
	private Clock c;
	private TextView soundPick;
	private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) { // TODO required C
		setContentView(R.layout.clock_view_activity); 
		id = getIntent().getExtras().getLong("id");
		System.out.println(id);
		initComponents();
		Formatter form;
		minutes.setFormatter(form = new Formatter() {

			@Override
			public String format(int value) {
				if (value < 10) {
					return "0" + value;
				}
				return String.valueOf(value);
			}
		});
		hours.setFormatter(form);
		super.onCreate(savedInstanceState);
	}

	private void initComponents() {
		name = (EditText) findViewById(R.id.clock_view_name);
		hours = (NumberPicker) findViewById(R.id.clock_view_hours_picker);
		minutes = (NumberPicker) findViewById(R.id.clock_view_minutes_picker);
		MO = (ToggleButton) findViewById(R.id.clock_button_MO);
		TU = (ToggleButton) findViewById(R.id.clock_button_TU);
		WE = (ToggleButton) findViewById(R.id.clock_button_WE);
		TH = (ToggleButton) findViewById(R.id.clock_button_TH);
		FR = (ToggleButton) findViewById(R.id.clock_button_FR);
		SA = (ToggleButton) findViewById(R.id.clock_button_SA);
		SU = (ToggleButton) findViewById(R.id.clock_button_SU);
		minutes.setMaxValue(59);
		minutes.setMinValue(0);
		hours.setMaxValue(23);
		hours.setMinValue(0);
		if (id != 0) {
			c = db.getClock((long)id);
			name.setText(c.getName());
			hours.setValue(c.getHour());
			minutes.setValue(c.getMinutes());
			MO.setChecked(c.getRepeat()[0] == 1? true : false);
			TU.setChecked(c.getRepeat()[1] == 1? true : false);
			WE.setChecked(c.getRepeat()[2] == 1? true : false);
			TH.setChecked(c.getRepeat()[3] == 1? true : false);
			FR.setChecked(c.getRepeat()[4] == 1? true : false);
			SA.setChecked(c.getRepeat()[5] == 1? true : false);
			SU.setChecked(c.getRepeat()[6] == 1? true : false);
		}
		save = (Button) findViewById(R.id.clock_view_ok);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (c == null) {
					c = new Clock();
				}
				c.setHour(hours.getValue());
				c.setMinutes(minutes.getValue());
				c.setName(name.getText().toString());
				c.setActive(true);
				c.setSound(sound); //TODO sound picker view
				c.setRepeat(getRepeats());

				if (c.getId() == -1) {
					c.setId(db.createClock(c));
				} else {
					db.updateClock(c);
				}
				ClockService.updateClock(getApplicationContext(), c.getId());
				setResult(10);
				finish();
			}
		});
		delete = (Button) findViewById(R.id.clock_view_cancel);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (c != null) {
					db.deleteClock(c.getId());
				}
				setResult(10);
				finish();
			}
		});
		soundPick = (TextView) findViewById(R.id.clock_view_sound_pick);
		soundPick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentR = new Intent(
						RingtoneManager.ACTION_RINGTONE_PICKER);

				startActivityForResult(intentR, 999);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode != RESULT_OK) {

			return;
		}
		if (requestCode == 999) {
			sound = intent
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI); //TODO default
		}

		super.onActivityResult(requestCode, resultCode, intent);
	}

	private int[] getRepeats() {
		int[] repeats = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		if (MO.isChecked()) {
			repeats[0] = 1;
		}
		if (TU.isChecked()) {
			repeats[1] = 1;
		}
		if (WE.isChecked()) {
			repeats[2] = 1;
		}
		if (TH.isChecked()) {
			repeats[3] = 1;
		}
		if (FR.isChecked()) {
			repeats[4] = 1;
		}
		if (SA.isChecked()) {
			repeats[5] = 1;
		}
		if (SU.isChecked()) {
			repeats[6] = 1;
		}
		return repeats;
	}
}
