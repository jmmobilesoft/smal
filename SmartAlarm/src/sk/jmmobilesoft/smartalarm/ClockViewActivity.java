package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.service.ClockSetting;
import sk.jmmobilesoft.smartalarm.service.Helper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ClockViewActivity extends Activity {

	DBHelper db = new DBHelper(this);

	private MediaPlayer mp;

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
	private Uri sound = null;
	private Button save;
	private Button delete;
	private Clock c;
	private TextView soundPick;
	private TextView soundName;
	private Long id;
	private SeekBar volumeBar;
	private AudioManager mAudioManager;
	private int originalVolume;
	private float volume = 0.3f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.clock_view_activity);
		id = getIntent().getExtras().getLong("id");
		System.out.println("clock id:" + id);
		initComponents();
		minutes.setFormatter(Helper.getNumberPickFormater());
		hours.setFormatter(Helper.getNumberPickFormater());
		super.onCreate(savedInstanceState);
	}

	private void initComponents() {
		RelativeLayout screen = (RelativeLayout) findViewById(R.id.clock_view_activity_screen);
		name = (EditText) findViewById(R.id.clock_view_activity_name);
		hours = (NumberPicker) findViewById(R.id.clock_view_activity_hours_picker);
		minutes = (NumberPicker) findViewById(R.id.clock_view_activity_minutes_picker);
		MO = (ToggleButton) findViewById(R.id.clock_view_activity_button_MO);
		TU = (ToggleButton) findViewById(R.id.clock_view_activity_button_TU);
		WE = (ToggleButton) findViewById(R.id.clock_view_activity_button_WE);
		TH = (ToggleButton) findViewById(R.id.clock_view_activity_button_TH);
		FR = (ToggleButton) findViewById(R.id.clock_view_activity_button_FR);
		SA = (ToggleButton) findViewById(R.id.clock_view_activity_button_SA);
		SU = (ToggleButton) findViewById(R.id.clock_view_activity_button_SU);
		soundName = (TextView) findViewById(R.id.clock_view_activity_sound_name);
		volumeBar = (SeekBar) findViewById(R.id.clock_view_activity_volume_picker);
		minutes.setMaxValue(59);
		minutes.setMinValue(0);
		hours.setMaxValue(23);
		hours.setMinValue(0);
		new Color();
		Helper.setNumberPickerTextColor(hours, Color.rgb(247, 245, 245));
		Helper.setNumberPickerTextColor(minutes, Color.rgb(247, 245, 245));
		volumeBar.setProgress(0);
		if (id != 0) {
			c = db.getClock((long) id);
			name.setText(c.getName());
			hours.setValue(c.getHour());
			minutes.setValue(c.getMinutes());
			MO.setChecked(c.getRepeat()[0] == 1 ? true : false);
			TU.setChecked(c.getRepeat()[1] == 1 ? true : false);
			WE.setChecked(c.getRepeat()[2] == 1 ? true : false);
			TH.setChecked(c.getRepeat()[3] == 1 ? true : false);
			FR.setChecked(c.getRepeat()[4] == 1 ? true : false);
			SA.setChecked(c.getRepeat()[5] == 1 ? true : false);
			SU.setChecked(c.getRepeat()[6] == 1 ? true : false);
			sound = c.getSound();
			if (c.getVolume() != 0) {
				volumeBar.setProgress((int) (c.getVolume() * 100));
			}
		}
		if (sound == null
				|| sound.compareTo(Uri
						.parse("android.resource://sk.jmmobilesoft.smartalarm/"
								+ R.raw.alarm)) == 0) {
			soundName.setText("default");
			sound = Uri.parse("android.resource://sk.jmmobilesoft.smartalarm/"
					+ R.raw.alarm);
		} else {
			soundName.setText(getSongName(sound));
		}
		if (volumeBar.getProgress() == 0) {
			volumeBar.setProgress((int) (volume * 100));
		}
		save = (Button) findViewById(R.id.clock_view_activity_save);
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
				if (sound != null) {
					c.setSound(sound);
				}
				c.setVolume(volume);
				c.setRepeat(getRepeats());

				if (c.getId() == -1) {
					c.setId(db.createClock(c));
				} else {
					db.updateClock(c);
				}
				System.out.println(c);
				if (ClockSetting.getDayRepeat(c)) {
					System.out.println("setting clock");
					ClockSetting.setClock(getApplicationContext(), c.getId());
				}
				try {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							originalVolume, 0);
					mp.stop();
					mp.release();
					mp.reset();
				} catch (NullPointerException | IllegalStateException e) {
					Log.i("INFO", "media player already stopped");
				}
				setResult(10);
				finish();
			}
		});
		delete = (Button) findViewById(R.id.clock_view_activity_cancel); // TODO
																			// deleting
																			// active
																			// alarm?
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (c != null) {
					db.deleteClock(c.getId());
				}
				try {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							originalVolume, 0);
					mp.stop();
					mp.release();
					mp.reset();
				} catch (NullPointerException | IllegalStateException e) {
					Log.i("INFO", "media player already stopped");
				}
				setResult(10);
				finish();
			}
		});
		soundPick = (TextView) findViewById(R.id.clock_view_activity_sound_pick);
		soundPick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ClockViewActivity.this);
				builder.setTitle("Select type").setItems(
						new String[] { "Alarm tone", "Music" },
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									Intent intentR = new Intent(
											RingtoneManager.ACTION_RINGTONE_PICKER);
									intentR.putExtra(
											RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,
											false);
									intentR.putExtra(
											RingtoneManager.EXTRA_RINGTONE_TITLE,
											"Choose alarm sound");
									startActivityForResult(intentR, 999);
								}
								if (which == 1) {
									Intent intentR = new Intent(
											Intent.ACTION_PICK,
											android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
									startActivityForResult(intentR, 998);
								}
							}
						});
				builder.create();
				builder.show();

			}
		});
		volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				System.out.println("progress:" + seekBar.getProgress());
				try {
					mp.stop();
					mp.release();
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							originalVolume, 0);
				} catch (NullPointerException | IllegalStateException e) {
					Log.i("INFO", "media player already stopped");
				}
				mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
				originalVolume = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						mAudioManager
								.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
						0);
				mp = MediaPlayer.create(getApplicationContext(), sound);
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				volume = determineVolume(seekBar.getProgress());
				mp.setVolume(volume, volume);
				mp.start();
			}
		});
		screen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							originalVolume, 0);
					mp.stop();
					mp.release();
					mp.reset();
				} catch (NullPointerException | IllegalStateException e) {
					Log.i("INFO", "media player already stopped");
				}
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
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI); 
		}
		if (requestCode == 998) {
			sound = intent.getData();
		}
		soundName.setText(getSongName(sound));
		super.onActivityResult(requestCode, resultCode, intent);
	}

	private float determineVolume(int seekbarStatus) {
		final float MIN = 0.2f;
		float volume = (float) (seekbarStatus * 0.01);
		System.out.println(volume);
		if (volume < MIN) {
			return 0.1f;
		}
		return volume;
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
	
	private String getSongName(Uri uri) {
		String[] projection = { MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE };

		Cursor cursor = this.managedQuery(uri, projection, null, null, null);

		cursor.moveToNext();
		String artist = cursor.getString(0);
		String title = cursor.getString(1);

		if(!artist.equals("<unknown>")){
			title = artist + " - " + title;
		}
		
		return title;
	}	
}
