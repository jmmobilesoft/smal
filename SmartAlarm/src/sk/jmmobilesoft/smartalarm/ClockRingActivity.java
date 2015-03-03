package sk.jmmobilesoft.smartalarm;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.ClockHelper;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.service.ClockSetting;
import sk.jmmobilesoft.smartalarm.service.WeatherRefreshService;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ClockRingActivity extends Activity {

	private int originalVolume;

	private MediaPlayer mediaPlayer;

	private AudioManager audioManager;

	private Vibrator v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("ClockRingActivity: started");
		if (GlobalHelper.isMyServiceRunning(WeatherRefreshService.class,
				getApplicationContext())) {
			// TODO get weather service if it's running and end it
		}
		Logger.serviceInfo("start");
		Helper.wakeLockOn(this); // TODO remove
		Logger.serviceInfo("wake lock");
		setWindow();
		Logger.serviceInfo("window");
		setView();
		Logger.serviceInfo("view");
		GlobalHelper.hideActionBar(this);
		Logger.serviceInfo("ClockRingActivity: fully started");

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		Helper.wakeLockOff(this);
		super.onPause();
	}

	@Override
	public void onDetachedFromWindow() {
		try {
			stopMediaPlayer();
		} catch (NullPointerException | IllegalStateException e) {
			System.out.println("mediaplayer stopped");
		}
		super.onDetachedFromWindow();
	}

	private void setView() {
		setContentView(R.layout.clock_ring_activity);
		DBHelper db = new DBHelper(this);
		long id = getIntent().getLongExtra("ID", -1l);
		Clock c = db.getClock(id);
		originalVolume = startMediaPlayer(c);

		TextView time = (TextView) findViewById(R.id.ring_clock);
		TextView name = (TextView) findViewById(R.id.ring_name);
		TextView date = (TextView) findViewById(R.id.ring_date);
		SeekBar dismiss = (SeekBar) findViewById(R.id.ring_seek_dismiss);
		SeekBar snooze = (SeekBar) findViewById(R.id.ring_seek_snooze);
		LinearLayout buttons = (LinearLayout) findViewById(R.id.clock_ring_activity_buttons_supercontainer);
		RelativeLayout weatherLayout = (RelativeLayout) findViewById(R.id.ring_weather_1_container);
		ImageView icon = (ImageView) findViewById(R.id.clock_ring_activity_image_view);

		setTexts(time, name, c, date);
		setWeatherContainer(c, db, weatherLayout, icon);
		setSeekBars(buttons, c, db, weatherLayout, dismiss, snooze);
		setButtons(buttons);
	}

	private void setSeekBars(final LinearLayout buttons, final Clock c,
			final DBHelper db, final RelativeLayout weatherLayout,
			final SeekBar dismiss, final SeekBar snooze) {
		dismiss.setProgress(0);
		dismiss.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (seekBar.getProgress() > 80) {
					if (Arrays.equals(c.getRepeat(), new int[] { 0, 0, 0, 0, 0,
							0, 0 })) {
						c.setActive(false);
						db.updateClock(c);
					}

					stopMediaPlayer();
					if (weatherLayout.getVisibility() == View.GONE) {
						finish();
					}
					dismiss.setVisibility(View.GONE);
					snooze.setVisibility(View.GONE);
					buttons.setVisibility(LinearLayout.VISIBLE);
				}
				seekBar.setProgress(0);
				ClockHelper.determineAlarmIcon(getApplicationContext());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});

		snooze.setRotation(180);
		snooze.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (seekBar.getProgress() > 80) {
					ClockSetting.setSnoozeClock(getApplicationContext(),
							c.getId());
					c.setActive(false);
					db.updateClock(c);
					finish();
				} else {
					seekBar.setProgress(0);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});
	}

	private void setTexts(TextView time, TextView name, Clock c, TextView date) {
		Calendar current = Helper.getCurrentTime();
		time.setText(Helper.format(current.get(Calendar.HOUR_OF_DAY)) + ":"
				+ Helper.format(current.get(Calendar.MINUTE)));
		name.setText(c.getName());
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		date.setText(format.format(Helper.getCurrentTime().getTime()));
	}

	private void setButtons(LinearLayout buttons) {
		buttons.setVisibility(LinearLayout.INVISIBLE);
		Button close = (Button) findViewById(R.id.clock_ring_activity_close_button);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button weather = (Button) findViewById(R.id.clock_ring_activity_show_button);
		weather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplication(), MainActivity.class);
				i.putExtra("tab", "weather");
				startActivity(i);
				finish();
			}
		});
	}

	private void setWeatherContainer(Clock c, DBHelper db,
			RelativeLayout weatherLayout, ImageView icon) {
		RelativeLayout weatherLayout2 = (RelativeLayout) findViewById(R.id.ring_weather_2_container);
		int resourceIdWeather = getResources().getIdentifier(
				"clock_ring_image", "drawable", this.getPackageName());
		icon.setImageDrawable(getResources().getDrawable(resourceIdWeather));
		if (!c.getCities().isEmpty()) {
			icon.setVisibility(View.GONE);
			ImageView iw = (ImageView) findViewById(R.id.ring_weather_1_image_view);
			TextView temp1 = (TextView) findViewById(R.id.ring_weather_1_temperature_text);
			TextView sunset1 = (TextView) findViewById(R.id.ring_weather_1_sunset_text);
			TextView sunrise1 = (TextView) findViewById(R.id.ring_weather_1_sunrise_text);
			TextView wind1 = (TextView) findViewById(R.id.ring_weather_1_wind_text);
			TextView humidity1 = (TextView) findViewById(R.id.ring_weather_1_humidity_text);
			TextView min1 = (TextView) findViewById(R.id.ring_weather_1_mintemp_text);
			TextView max1 = (TextView) findViewById(R.id.ring_weather_1_maxtemp_text);
			TextView description1 = (TextView) findViewById(R.id.ring_weather_1_desription_text);
			TextView city1 = (TextView) findViewById(R.id.ring_weather_1_city_name);
			TextView update1 = (TextView) findViewById(R.id.ring_weather_1_update_time);
			WeatherForecast w = db.getWeatherForecastByCity(c.getCities()
					.get(0));
			int resourceId = getResources().getIdentifier("w" + w.getIcon(),
					"drawable", getPackageName());
			iw.setImageDrawable(getResources().getDrawable(resourceId));
			temp1.setText(Float.toString(Helper.kelvinToCelsius(w
					.getTemperature())) + "°C");
			sunset1.setText("Sunset: " + Helper.milisToTime(w.getSunset()));
			sunrise1.setText("Sunrise: " + Helper.milisToTime(w.getSunrise()));
			wind1.setText("Speed: " + w.getWindSpeed() + "m/s");
			humidity1.setText("Humidity: " + w.getHumidity() + "%");
			min1.setText("Min: " + Helper.kelvinToCelsius(w.getTempMin()) + "°"
					+ "C");
			max1.setText("Max: " + Helper.kelvinToCelsius(w.getTempMax()) + "°"
					+ "C");
			description1.setText(w.getDescription());
			city1.setText(w.getCityName());
			Calendar lastUpdate = ClockHelper.calendarFromString(w
					.getUpdateTime());
			lastUpdate.add(Calendar.HOUR_OF_DAY, 1);
			Logger.serviceInfo(lastUpdate.getTime() + " - "
					+ Calendar.getInstance().getTime());
			if (lastUpdate.after(Calendar.getInstance())) {
				update1.setText("last hour");
			} else {
				update1.setText(w.getUpdateTime());
			}

			if (c.getCities().size() == 2) {
				ImageView iw2 = (ImageView) findViewById(R.id.ring_weather_2_image_view);
				TextView temp2 = (TextView) findViewById(R.id.ring_weather_2_temperature_text);
				TextView sunset2 = (TextView) findViewById(R.id.ring_weather_2_sunset_text);
				TextView sunrise2 = (TextView) findViewById(R.id.ring_weather_2_sunrise_text);
				TextView wind2 = (TextView) findViewById(R.id.ring_weather_2_wind_text);
				TextView humidity2 = (TextView) findViewById(R.id.ring_weather_2_humidity_text);
				TextView min2 = (TextView) findViewById(R.id.ring_weather_2_mintemp_text);
				TextView max2 = (TextView) findViewById(R.id.ring_weather_2_maxtemp_text);
				TextView description2 = (TextView) findViewById(R.id.ring_weather_2_desription_text);
				TextView city2 = (TextView) findViewById(R.id.ring_weather_2_city_name);
				TextView update2 = (TextView) findViewById(R.id.ring_weather_2_update_time);

				WeatherForecast w2 = db.getWeatherForecastByCity(c.getCities()
						.get(1));
				int resourceId2 = getResources().getIdentifier(
						"w" + w2.getIcon(), "drawable", getPackageName());
				iw2.setImageDrawable(getResources().getDrawable(resourceId2));
				temp2.setText(Float.toString(Helper.kelvinToCelsius(w2
						.getTemperature())) + "°C");
				sunset2.setText("Sunset: " + Helper.milisToTime(w2.getSunset()));
				sunrise2.setText("Sunrise: "
						+ Helper.milisToTime(w2.getSunrise()));
				wind2.setText("Speed: " + w2.getWindSpeed() + "m/s");
				humidity2.setText("Humidity: " + w2.getHumidity() + "%");
				min2.setText("Min: " + Helper.kelvinToCelsius(w2.getTempMin())
						+ "°" + "C");
				max2.setText("Max: " + Helper.kelvinToCelsius(w2.getTempMax())
						+ "°" + "C");
				description2.setText(w2.getDescription());
				city2.setText(w2.getCityName());
				update2.setText(w2.getUpdateTime());
			} else {
				weatherLayout2.setVisibility(View.GONE);
			}
		} else {
			weatherLayout.setVisibility(View.GONE);
			weatherLayout2.setVisibility(View.GONE);
			icon.setVisibility(View.VISIBLE);
		}
	}

	private void setWindow() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
	}

	private int startMediaPlayer(Clock c) {
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int originalVolume = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		audioManager.setSpeakerphoneOn(true);
		mediaPlayer = MediaPlayer.create(this, c.getSound());
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(true);
		mediaPlayer.setVolume(c.getVolume(), c.getVolume());
		mediaPlayer.start();
		startVibrator(c);
		return originalVolume;
	}

	private void stopMediaPlayer() {
		stopVibrator();
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume,
				0);
		mediaPlayer.stop();
		mediaPlayer.reset();
		mediaPlayer.release();
	}

	private void startVibrator(Clock c) {
		if (c.isVibrate()) {
			v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			v.vibrate(new long[] { 1000, 1000 }, 0);
		}
	}

	private void stopVibrator() {
		if (v != null) {
			v.cancel();
		}
	}
}
