package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.ClockHelper;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.service.ClockSetting;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ClockViewActivity extends Activity {

	private MediaPlayer mp;
	private TextView soundName;
	private TextView weatherCities;
	private Uri sound = null;
	private List<String> cities;
	ToggleButton MO;
	ToggleButton TU;
	ToggleButton WE;
	ToggleButton TH;
	ToggleButton FR;
	ToggleButton SA;
	ToggleButton SU;
	private int originalVolume;

	private AudioManager mAudioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("ClockViewActivity: started");
		GlobalHelper.hideActionBar(this);
		setView();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onUserInteraction() {
		GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
		super.onUserInteraction();
	}

	@Override
	protected void onPause() {
		GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
		super.onPause();
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
			soundName.setText(getSongName(sound));
		}
		if (requestCode == 998) {
			sound = intent.getData();
			soundName.setText(getSongName(sound));
		}
		if (requestCode == 111) {
			Bundle b = intent.getBundleExtra("result");
			List<String> cities = new ArrayList<>();
			DBHelper db = new DBHelper(getApplicationContext());
			List<Integer> ids = b.getIntegerArrayList("weathers");
			for (int i = 0; i < ids.size(); i++) {
				cities.add(db.getWeatherForecast(ids.get(i)).getCityName());
			}
			this.cities = cities;
			weatherCities.setText(cities.toString().replace("[", "")
					.replace("]", ""));
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	public void setView() {
		setContentView(R.layout.clock_view_activity);
		EditText name = (EditText) findViewById(R.id.clock_view_activity_name);
		TextView snooze = (TextView) findViewById(R.id.clock_view_activity_snooze_number);
		CheckBox vibrate = (CheckBox) findViewById(R.id.clock_view_activity_vibrator_checkbox);
		weatherCities = (TextView) findViewById(R.id.clock_view_activity_weather_name);
		NumberPicker hours = (NumberPicker) findViewById(R.id.clock_view_activity_hours_picker);
		NumberPicker minutes = (NumberPicker) findViewById(R.id.clock_view_activity_minutes_picker);
		MO = (ToggleButton) findViewById(R.id.clock_view_activity_button_MO);
		TU = (ToggleButton) findViewById(R.id.clock_view_activity_button_TU);
		WE = (ToggleButton) findViewById(R.id.clock_view_activity_button_WE);
		TH = (ToggleButton) findViewById(R.id.clock_view_activity_button_TH);
		FR = (ToggleButton) findViewById(R.id.clock_view_activity_button_FR);
		SA = (ToggleButton) findViewById(R.id.clock_view_activity_button_SA);
		SU = (ToggleButton) findViewById(R.id.clock_view_activity_button_SU);
		soundName = (TextView) findViewById(R.id.clock_view_activity_sound_name);
		SeekBar volumeBar = (SeekBar) findViewById(R.id.clock_view_activity_volume_picker);
		setNumberPickers(hours, minutes);
		cities = new ArrayList<String>();
		long id = getIntent().getExtras().getLong("id");
		DBHelper db = new DBHelper(getApplicationContext());
		Clock c;
		if (id != -1) {
			c = db.getClock(id);
			setComponentsOldClock(c, name, hours, minutes, volumeBar, snooze);
		} else {
			c = new Clock();
			setComponentsNewClock(c, hours, minutes, volumeBar, snooze);
		}
		setSnoozeButtons(c, snooze);
		setSoundPickContainer();
		setVibrateContainer(vibrate, c);
		setVolumeContainer(volumeBar);
		setWeatherContainer(c);
		setControlButtons(c, db, name, hours, minutes, vibrate, volumeBar,
				snooze);
	}

	private void setNumberPickers(NumberPicker hours, NumberPicker minutes) {
		minutes.setMaxValue(59);
		minutes.setMinValue(0);
		hours.setMaxValue(23);
		hours.setMinValue(0);
		minutes.setFormatter(GlobalHelper.getNumberPickFormater());
		hours.setFormatter(GlobalHelper.getNumberPickFormater());
		GlobalHelper.setNumberPickerTextColor(hours, Color.rgb(247, 245, 245));
		GlobalHelper
				.setNumberPickerTextColor(minutes, Color.rgb(247, 245, 245));
	}

	private void setComponentsNewClock(Clock c, NumberPicker hours,
			NumberPicker minutes, SeekBar volumeBar, TextView snooze) {
		snooze.setText(String.valueOf(5));
		c.setSnoozeTime(5);
		hours.setValue(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		minutes.setValue(Calendar.getInstance().get(Calendar.MINUTE));
		volumeBar.setProgress((int) (0.3 * 100));
		getSoundText(soundName);
	}

	private void setComponentsOldClock(Clock c, EditText name,
			NumberPicker hours, NumberPicker minutes, SeekBar volumeBar,
			TextView snooze) {
		snooze.setText(String.valueOf(c.getSnoozeTime()));
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
		volumeBar.setProgress((int) (c.getVolume() * 100));
		sound = c.getSound();
		getSoundText(soundName);
		cities = c.getCities();
	}

	private void setSnoozeButtons(final Clock c, final TextView snooze) {
		final Button plus = (Button) findViewById(R.id.clock_view_activity_snooze_plus_button);
		final Button minus = (Button) findViewById(R.id.clock_view_activity_snooze_minus_button);
		plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (c.getSnoozeTime() == 60) {
					plus.setClickable(false);
				}
				if (c.getSnoozeTime() <= 59) {
					c.setSnoozeTime(c.getSnoozeTime() + 1);
				}
				minus.setClickable(true);
				snooze.setText(String.valueOf(c.getSnoozeTime()));
			}
		});
		minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (c.getSnoozeTime() == 1) {
					minus.setClickable(false);
					;
				}
				if (c.getSnoozeTime() >= 2) {
					c.setSnoozeTime(c.getSnoozeTime() - 1);
					;
				}
				plus.setClickable(true);
				snooze.setText(String.valueOf(c.getSnoozeTime()));
			}
		});
	}

	private void setControlButtons(final Clock c, final DBHelper db,
			final EditText name, final NumberPicker hours,
			final NumberPicker minutes, final CheckBox vibrate,
			final SeekBar volumeBar, final TextView snooze) {
		setSaveButton(c, hours, minutes, name, vibrate, volumeBar, snooze, db);
		setDeleteButton(c, db);
	}

	private void setSaveButton(final Clock c, final NumberPicker hours,
			final NumberPicker minutes, final EditText name,
			final CheckBox vibrate, final SeekBar volumeBar,
			final TextView snooze, final DBHelper db) {
		Button save = (Button) findViewById(R.id.clock_view_activity_save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				c.setHour(hours.getValue());
				c.setMinutes(minutes.getValue());
				c.setName(name.getText().toString());
				c.setActive(true);
				c.setSound(sound);
				c.setVibrate(vibrate.isChecked());
				c.setVolume(GlobalHelper.determineVolume(volumeBar
						.getProgress()));
				c.setRepeat(getRepeats());
				c.setSnoozeTime(Integer.valueOf(snooze.getText().toString()));
				c.setCities(cities);
				Clock dbClock;
				if ((dbClock = db.getClockByTime(hours.getValue(),
						minutes.getValue())) == null
						|| !ClockHelper.compareRepeats(dbClock, c)) {
					if (c.getId() == -1) {
						c.setId(db.createClock(c));
					} else {
						db.updateClock(c);
					}
					Logger.setInfo("Setting clock:" + c);
					boolean t = ClockSetting.setClock(getApplicationContext(),
							c.getId());
					if (t) {
						Helper.showToast(c, getApplicationContext());
					}
				} else {
					Helper.createToast(getApplicationContext(),
							"Alarm for this time already exist!");
				}
				GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
				setResult(10);
				finish();
			}
		});
	}

	private void setDeleteButton(final Clock c, final DBHelper db) {
		Button delete = (Button) findViewById(R.id.clock_view_activity_cancel);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (c.getId() != -1) {
					ClockSetting.deactivateClock(c, getApplicationContext());
					db.deleteClock(c.getId());
					ClockHelper.determineAlarmIcon(getApplicationContext());
				}
				GlobalHelper.stopMediaPlayer(mAudioManager, originalVolume, mp);
				setResult(10);
				finish();
			}
		});
	}

	private void setSoundPickContainer() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.clock_view_activity_ringtone_container);
		layout.setOnClickListener(new OnClickListener() {

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
	}

	private void setVibrateContainer(final CheckBox vibrate, Clock c) {
		vibrate.setChecked(c.isVibrate());
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.clock_view_activity_vibrator_container);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vibrate.setChecked(!vibrate.isChecked());

			}
		});
	}

	private void setVolumeContainer(SeekBar volumeBar) {
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

	private void setWeatherContainer(Clock c) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.clock_view_activity_weather_container);

		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentA = new Intent(getApplicationContext(),
						WeatherSelectActivity.class);
				startActivityForResult(intentA, 111);
			}
		});
		weatherCities.setText(c.getCities().toString().replace("[", "")
				.replace("]", ""));
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
			soundName.setText(getSongName(sound));
		}
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
		String[] projection = { MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.TITLE };

		Cursor cursor = this.managedQuery(uri, projection, null, null, null);

		cursor.moveToNext();
		String artist = cursor.getString(0);
		String title = cursor.getString(1);

		if (!artist.equals("<unknown>")) {
			title = artist + " - " + title;
		}

		return title;
	}
}
