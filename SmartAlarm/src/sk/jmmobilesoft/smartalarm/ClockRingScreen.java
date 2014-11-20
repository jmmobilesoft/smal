package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import sk.jmmobilesoft.smartalarm.service.ClockSetting;
import sk.jmmobilesoft.smartalarm.service.Helper;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ClockRingScreen extends Activity {

	private long id;

	private DBHelper db;

	private MediaPlayer mp;

	private AudioManager mAudioManager;

	private int originalVolume;

	private Context context;
	
//	private SeekBar dismiss;
//	private SeekBar snooze;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("INFO", "ClockRingScreen activity started");
		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED // START
																			// DISPLAY
																			// etc
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		db = new DBHelper(this);
		setContentView(R.layout.clock_ring_activity);
		id = getIntent().getLongExtra("ID", -1);
		final Clock c = db.getClock(id);
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		originalVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		mp = MediaPlayer.create(this, c.getSound());
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setLooping(true);
		mp.setVolume(c.getVolume(), c.getVolume());
		mp.start();
		System.out.println(c.getSound());
		TextView time = (TextView) findViewById(R.id.ring_clock);
		time.setText(Helper.format(c.getHour()) + ":"
				+ Helper.format(c.getMinutes()));

		TextView name = (TextView) findViewById(R.id.ring_name);
		name.setText(c.getName());
		context = this;
		
		setWeather(c);

		final SeekBar dismiss = (SeekBar) findViewById(R.id.ring_seek_dismiss);
		final SeekBar snooze = (SeekBar) findViewById(R.id.ring_seek_snooze);
		
		
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
					dismiss.setVisibility(View.GONE);
					snooze.setVisibility(View.GONE);
					//finish();
				}
				seekBar.setProgress(0);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {}
		});

		
		snooze.setRotation(180);
		snooze.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (seekBar.getProgress() > 80) {
					ClockSetting.setSnoozeClock(context, c.getId());
					if (Arrays.equals(c.getRepeat(), new int[] { 0, 0, 0, 0, 0,
							0, 0 })) {
						c.setActive(false);
						db.updateClock(c);
					}
					finish();
				} else {
					seekBar.setProgress(0);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {}
		});
		super.onCreate(savedInstanceState);
	}

	private void setWeather(Clock c){
		TextView temp = (TextView) findViewById(R.id.ring_temperature_text);
		TextView sunset = (TextView) findViewById(R.id.ring_sunset_text);
		TextView sunrise = (TextView) findViewById(R.id.ring_sunrise_text);
		TextView wind = (TextView) findViewById(R.id.ring_wind_text);
		TextView humidity = (TextView) findViewById(R.id.ring_humidity_text);
		TextView maxmin = (TextView) findViewById(R.id.ring_maxmintemp_text );
		TextView description = (TextView) findViewById(R.id.ring_desription_text);
		TextView city = (TextView) findViewById(R.id.ring_city_name);
		TextView update = (TextView) findViewById(R.id.ring_update_time);
		
		WeatherForecast w = db.getWeatherByCity(c.getCities().get(0));
		temp.setText(Float.toString(Helper.kelvinToCelsius(w.getTemperature())) + "°C");
		sunset.setText(Helper.milisToTime(w.getSunset()));
		sunrise.setText(Helper.milisToTime(w.getSunrise()));
		wind.setText(w.getWindSpeed() + "m/s");
		humidity.setText(w.getHumidity() + "%");
		maxmin.setText(Helper.kelvinToCelsius(w.getTempMin()) + "/"
				+ Helper.kelvinToCelsius(w.getTempMax()) + "°" + "C");
		description.setText(w.getDecsription());
		city.setText(w.getCityName());
		update.setText(w.getUpdateTime());
		
	}
	
	@Override
	protected void onPause() {
		Helper.wakeLockOff(this);
		super.onPause();
	}

	@Override
	public void onDetachedFromWindow() {
		try{
			stopMediaPlayer();
		}catch(NullPointerException | IllegalStateException e){
			System.out.println("mediaplayer stopped");
		}
		super.onDetachedFromWindow();
	}
	
	private void stopMediaPlayer(){
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				originalVolume, 0);
		mp.stop();
		mp.reset();
		mp.release();
	}
}
