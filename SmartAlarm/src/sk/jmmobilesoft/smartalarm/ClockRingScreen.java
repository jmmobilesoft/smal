package sk.jmmobilesoft.smartalarm;

import java.util.Arrays;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.service.Helper;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClockRingScreen extends Activity {

	private long id;

	private DBHelper db;

	private MediaPlayer mp;
	
	private AudioManager mAudioManager;
	
	private int originalVolume;

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
		originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
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

		Button snooze = (Button) findViewById(R.id.ring_snooze);
		snooze.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO snooze time
				finish();
			}
		});

		Button dismiss = (Button) findViewById(R.id.ring_dismiss);
		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Arrays.equals(c.getRepeat(), new int[] { 0, 0, 0, 0, 0, 0,
						0 })) {
					c.setActive(false);
					db.updateClock(c);
				}
				finish();
			}
		});
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		Helper.wakeLockOff(this);
		super.onPause();
	}

	@Override
	public void onDetachedFromWindow() {
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
		mp.stop();
		mp.reset();
		mp.release();
		super.onDetachedFromWindow();
	}
}
