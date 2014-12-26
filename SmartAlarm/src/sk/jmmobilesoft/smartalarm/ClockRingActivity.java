package sk.jmmobilesoft.smartalarm;

import java.util.Arrays;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.ClockHelper;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.service.ClockSetting;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ClockRingActivity extends Activity {

	private int originalVolume;

	private MediaPlayer mediaPlayer;

	private AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("ClockRingActivity: started");
		GlobalHelper.hideActionBar(this);
		Helper.wakeLockOn(this); //TODO remove
		setWindow();
		setView();

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
		SeekBar dismiss = (SeekBar) findViewById(R.id.ring_seek_dismiss);
		SeekBar snooze = (SeekBar) findViewById(R.id.ring_seek_snooze);
		RelativeLayout weatherLayout = (RelativeLayout) findViewById(R.id.ring_weather_1_container);

		setTexts(time, name, c);
		setWeatherContainer(c, db, weatherLayout);
		setSeekBars(c, db, weatherLayout, dismiss, snooze);
	}

	private void setSeekBars(final Clock c, final DBHelper db,
			final RelativeLayout weatherLayout, final SeekBar dismiss,
			final SeekBar snooze) {
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

	private void setTexts(TextView time, TextView name, Clock c) {
		time.setText(Helper.format(c.getHour()) + ":"
				+ Helper.format(c.getMinutes()));
		name.setText(c.getName());
	}

	private void setWeatherContainer(Clock c, DBHelper db,
			RelativeLayout weatherLayout) {
		RelativeLayout weatherLayout2 = (RelativeLayout) findViewById(R.id.ring_weather_2_container);
		if (!c.getCities().isEmpty()) {
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
			WeatherForecast w = db.getWeatherForecastByCity(c.getCities().get(0));
			int resourceId = getResources().getIdentifier("w" + w.getIcon(),
					"drawable", getPackageName());
			iw.setImageDrawable(getResources().getDrawable(resourceId));
			temp1.setText(Float.toString(Helper.kelvinToCelsius(w
					.getTemperature())) + "°C");
			sunset1.setText("Sunset: " + Helper.milisToTime(w.getSunset()));
			sunrise1.setText("Sunrise: " + Helper.milisToTime(w.getSunrise()));
			wind1.setText("Speed: " + w.getWindSpeed() + "m/s");
			humidity1.setText("Humidity: " + w.getHumidity() + "%");
			min1.setText("Min: " + Helper.kelvinToCelsius(w.getTempMin()) + "°" + "C");
			max1.setText("Max: " + Helper.kelvinToCelsius(w.getTempMax()) + "°" + "C");
			description1.setText(w.getDescription());
			city1.setText(w.getCityName());
			update1.setText(w.getUpdateTime());

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

				WeatherForecast w2 = db.getWeatherForecastByCity(c.getCities().get(1));
				int resourceId2 = getResources().getIdentifier(
						"w" + w2.getIcon(), "drawable", getPackageName());
				iw2.setImageDrawable(getResources().getDrawable(resourceId2));
				temp2.setText(Float.toString(Helper.kelvinToCelsius(w2
						.getTemperature())) + "°C");
				sunset2.setText("Sunset: " + Helper.milisToTime(w2.getSunset()));
				sunrise2.setText("Sunrise: " + Helper.milisToTime(w2.getSunrise()));
				wind2.setText("Speed: " + w2.getWindSpeed() + "m/s");
				humidity2.setText("Humidity: " + w2.getHumidity() + "%");
				min2.setText("Min: " + Helper.kelvinToCelsius(w2.getTempMin()) + "°" + "C");
				max2.setText("Max: " + Helper.kelvinToCelsius(w2.getTempMax()) + "°" + "C");
				description2.setText(w2.getDescription());
				city2.setText(w2.getCityName());
				update2.setText(w2.getUpdateTime());
			} else {
				weatherLayout2.setVisibility(View.GONE);
			}
		} else {
			weatherLayout.setVisibility(View.GONE);
			weatherLayout2.setVisibility(View.GONE);
		}
	}

	private void setWindow() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	private int startMediaPlayer(Clock c) {
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int originalVolume = audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		mediaPlayer = MediaPlayer.create(this, c.getSound());
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(true);
		mediaPlayer.setVolume(c.getVolume(), c.getVolume());
		mediaPlayer.start();
		return originalVolume;
	}

	private void stopMediaPlayer() {
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume,
				0);
		mediaPlayer.stop();
		mediaPlayer.reset();
		mediaPlayer.release();
	}
}
