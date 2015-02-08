package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.database.TimerDAO;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Timer;
import sk.jmmobilesoft.smartalarm.service.TimerSetting;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TimerViewActivity extends Activity {

	private DBHelper db = new DBHelper(this);

	private MediaPlayer mp;
	private AudioManager mAudioManager;
	private int originalVolume;
	private TextView soundName;
	private Uri sound = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("TimerViewActivity.class: started");
		GlobalHelper.hideActionBar(this);
		setContentView(R.layout.timer_view_activity);
		initComponents();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onUserInteraction() {
		GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
		super.onUserInteraction();
	}

	private void initComponents() {
		final NumberPicker hours = (NumberPicker) findViewById(R.id.timer_view_activity_hours_picker);
		final NumberPicker minutes = (NumberPicker) findViewById(R.id.timer_view_activity_minutes_picker);
		final NumberPicker seconds = (NumberPicker) findViewById(R.id.timer_view_activity_seconds_picker);
		EditText name = (EditText) findViewById(R.id.timer_view_activity_name);
		soundName = (TextView) findViewById(R.id.timer_view_activity_sound_name);
		SeekBar volumeBar = (SeekBar) findViewById(R.id.timer_view_activity_volume_picker);

		setNumberPickers(hours, minutes, seconds);

		long id = getIntent().getExtras().getLong("id");
		volumeBar.setProgress(0);
		Timer t;
		if (id != -1) {
			t = db.getTimer(id);
			System.out.println(t);
			setComponentsOldTimer(name, hours, minutes, seconds, volumeBar, t);
		} else {
			t = new Timer();
			setComponentsNewTimer(volumeBar);
		}

		setSoundPickContainer();
		setVolumeBarContainer(volumeBar);
		setControlButtons(name, hours, minutes, seconds, volumeBar, t);
	}

	private void setComponentsOldTimer(EditText name, NumberPicker hours,
			NumberPicker minutes, NumberPicker seconds, SeekBar volumeBar,
			Timer t) {
		name.setText(t.getName());
		hours.setValue(t.getHours());
		minutes.setValue(t.getMinutes());
		seconds.setValue(t.getSeconds());
		sound = t.getSound();
		getSoundText(soundName);
		volumeBar.setProgress((int) t.getVolume() * 100);
	}

	private void setComponentsNewTimer(SeekBar volumeBar) {
		getSoundText(soundName);
		volumeBar.setProgress((int) (0.3 * 100));
	}

	private void setNumberPickers(NumberPicker hours, NumberPicker minutes,
			NumberPicker seconds) {
		minutes.setFormatter(GlobalHelper.getNumberPickFormater());
		hours.setFormatter(GlobalHelper.getNumberPickFormater());
		seconds.setFormatter(GlobalHelper.getNumberPickFormater());
		hours.setMaxValue(72);
		hours.setMinValue(0);
		minutes.setMaxValue(59);
		minutes.setMinValue(0);
		seconds.setMaxValue(59);
		seconds.setMinValue(0);
		GlobalHelper.setNumberPickerTextColor(hours, Color.rgb(247, 245, 245));
		GlobalHelper
				.setNumberPickerTextColor(minutes, Color.rgb(247, 245, 245));
		GlobalHelper
				.setNumberPickerTextColor(seconds, Color.rgb(247, 245, 245));
	}

	private void setControlButtons(EditText name, NumberPicker hours,
			NumberPicker minutes, NumberPicker seconds, SeekBar volumeBar,
			Timer t) {
		setSaveButton(name, hours, minutes, seconds, t, volumeBar);
		setDeleteButtons(t);
	}

	private void setDeleteButtons(final Timer t) {
		Button delete = (Button) findViewById(R.id.timer_view_activity_cancel);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (t != null) {
					TimerSetting.deactivateTimer(t, getApplicationContext());
					db.deleteTimer(t.getId());
				}
				GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
				setResult(10);
				Logger.serviceInfo("TimerViewActivity.class: finishing - delete");
				finish();
			}
		});
	}

	private void setSaveButton(final EditText name, final NumberPicker hours,
			final NumberPicker minutes, final NumberPicker seconds,
			final Timer t, final SeekBar volumeBar) {
		Button save = (Button) findViewById(R.id.timer_view_activity_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				t.setName(name.getText().toString());
				t.setHours(hours.getValue());
				t.setMinutes(minutes.getValue());
				t.setSeconds(seconds.getValue());
				t.setStart(Helper.getCurrentTime());
				t.setSound(sound);
				t.setVolume(GlobalHelper.determineVolume(volumeBar
						.getProgress()));
				t.setActive(true);
				if (db.getTimerByTime(hours.getValue(), minutes.getValue(),
						seconds.getValue()) == null) {
					if (t.getId() == -1) {
						t.setId(db.createTimer(t));
					} else {
						db.updateTimer(t);
					}
					TimerSetting.setTimer(getApplicationContext(), t.getId());
				} else {
					Helper.createToast(getApplicationContext(),
							"Timer for this duration already exist!");
				}
				GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
				setResult(11);
				Logger.serviceInfo("TimerViewActivity.class: finishing - save");
				finish();
			}
		});
	}

	private void setSoundPickContainer() {
		LinearLayout soundPick = (LinearLayout) findViewById(R.id.timer_view_activity_ringtone_container);
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
	}

	private void setVolumeBarContainer(SeekBar volumeBar) {
		volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
				mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
				originalVolume = mAudioManager
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						mAudioManager
								.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
						0);
				mp = MediaPlayer.create(getApplicationContext(), sound);
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				float volume = GlobalHelper.determineVolume(seekBar
						.getProgress());
				mp.setVolume(volume, volume);
				mp.start();
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
		soundName.setText(GlobalHelper.getSongName(sound, this));
		super.onActivityResult(requestCode, resultCode, intent);
	}

	private void getSoundText(TextView soundName) {
		if (sound == null
				|| sound.compareTo(Uri
						.parse("android.resource://sk.jmmobilesoft.smartalarm/"
								+ R.raw.alarm)) == 0) {
			soundName.setText("Default");
			sound = Uri.parse("android.resource://sk.jmmobilesoft.smartalarm/"
					+ R.raw.alarm);
		} else {
			soundName.setText(GlobalHelper.getSongName(sound, this));
		}
	}
}
