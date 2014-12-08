package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Timer;
import sk.jmmobilesoft.smartalarm.service.Helper;
import sk.jmmobilesoft.smartalarm.service.TimerSetting;
import android.app.ActionBar;
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
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TimerViewActivity extends Activity {

	private DBHelper db = new DBHelper(this);

	private MediaPlayer mp;
	private AudioManager mAudioManager;
	private int originalVolume;

	private long id;
	private EditText name;
	private NumberPicker hours;
	private NumberPicker minutes;
	private NumberPicker seconds;
	private Timer t;

	private TextView soundPick;
	private TextView soundName;
	private SeekBar volumeBar;
	private Uri sound = null;
	private float volume = 0.3f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("TimerViewActivity.class: started");
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		setContentView(R.layout.timer_view_activity);
		id = getIntent().getExtras().getLong("id");
		initComponents();
		minutes.setFormatter(Helper.getNumberPickFormater());
		hours.setFormatter(Helper.getNumberPickFormater());
		seconds.setFormatter(Helper.getNumberPickFormater());
		super.onCreate(savedInstanceState);
	}

	private void initComponents() {
		RelativeLayout screen = (RelativeLayout) findViewById(R.id.timer_view_activity_screen);
		hours = (NumberPicker) findViewById(R.id.timer_view_activity_hours_picker);
		minutes = (NumberPicker) findViewById(R.id.timer_view_activity_minutes_picker);
		seconds = (NumberPicker) findViewById(R.id.timer_view_activity_seconds_picker);
		name = (EditText) findViewById(R.id.timer_view_activity_name);
		soundName = (TextView) findViewById(R.id.timer_view_activity_sound_name);
		volumeBar = (SeekBar) findViewById(R.id.timer_view_activity_volume_picker);
		hours.setMaxValue(72);
		hours.setMinValue(0);
		minutes.setMaxValue(59);
		minutes.setMinValue(0);
		seconds.setMaxValue(59);
		seconds.setMinValue(0);
		Helper.setNumberPickerTextColor(hours, Color.rgb(247, 245, 245));
		Helper.setNumberPickerTextColor(minutes, Color.rgb(247, 245, 245));
		Helper.setNumberPickerTextColor(seconds, Color.rgb(247, 245, 245));
		volumeBar.setProgress(0);
		if (id != -1) {
			t = db.getTimer(id);
			name.setText(t.getName());
			hours.setValue(t.getHours());
			minutes.setValue(t.getMinutes());
			seconds.setValue(t.getSeconds());
			sound = t.getSound();
			volumeBar.setProgress((int) t.getVolume() * 100);
		}
		if (sound == null || sound.compareTo(Uri
				.parse("android.resource://sk.jmmobilesoft.smartalarm/"
						+ R.raw.timer)) == 0) {
			soundName.setText("Default");
			sound = Uri.parse("android.resource://sk.jmmobilesoft.smartalarm/"
					+ R.raw.timer);
		} else {
			soundName.setText(getSongName(sound));
		}
		if (volumeBar.getProgress() == 0) {
			volumeBar.setProgress((int) (volume * 100));
		}
		Button save = (Button) findViewById(R.id.timer_view_activity_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (t == null) {
					t = new Timer();
				}
				t.setName(name.getText().toString());
				t.setHours(hours.getValue());
				t.setMinutes(minutes.getValue());
				t.setSeconds(seconds.getValue());
				if (sound != null) {
					t.setSound(sound);
				}
				t.setVolume(volume);
				t.setActive(true);
				if (t.getId() == -1) {
					t.setId(db.createTimer(t));
				} else {
					db.updateTimer(t);
				}
				TimerSetting.setTimer(getApplicationContext(), t.getId());
				try {
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							originalVolume, 0);
					mp.stop();
					mp.release();
					mp.reset();
				} catch (NullPointerException | IllegalStateException e) {
					Log.i("INFO", "media player already stopped");
				}
				setResult(11);
				Logger.serviceInfo("TimerViewActivity.class: finishing - save");
				finish();
			}
		});
		Button delete = (Button) findViewById(R.id.timer_view_activity_cancel); // TODO
																				// deleting
																				// active
																				// alarm?
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (t != null) {
					db.deleteTimer(t.getId());
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
				Logger.serviceInfo("TimerViewActivity.class: finishing - delete");
				finish();
			}
		});
		soundPick = (TextView) findViewById(R.id.timer_view_activity_sound_pick);
		soundPick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TimerViewActivity.this);
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
		if (volume < MIN) {
			return 0.1f;
		}
		return volume;
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
