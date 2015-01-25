package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Timer;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TimerRingScreen extends Activity {

	private DBHelper db;

	private MediaPlayer mp;

	private AudioManager mAudioManager;
	
	private Vibrator v;

	private int originalVolume;

	private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("TimerRingScreen activity started");
		// Helper.wakeLockOn(this); TODO test

		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED // START
																			// DISPLAY
																			// etc
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		db = new DBHelper(this);
		setContentView(R.layout.timer_ring_activity);
		id = getIntent().getLongExtra("ID", -1);
		final Timer t = db.getTimer(id);
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		originalVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		mp = MediaPlayer.create(this, t.getSound());
		System.out.println("sound:" + t.getSound());
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setLooping(true);
		mp.setVolume(t.getVolume(), t.getVolume());
		mp.start();
		startVibrator();
		TextView name = (TextView) findViewById(R.id.timer_ring_activity_Name);
		if (name.equals("")) {
			name.setText("No specified name timer");
		} else {
			name.setText(t.getName());
		}

		Button ok = (Button) findViewById(R.id.timer_ring_activity_OK);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				t.setActive(false);
				db.updateTimer(t);
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
		stopVibrator();
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				originalVolume, 0);
		mp.stop();
		mp.reset();
		mp.release();
		super.onDetachedFromWindow();
	}

	private void startVibrator() {
		v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		v.vibrate(new long[] { 1000, 1000 }, 0);
	}

	private void stopVibrator() {
		v.cancel();
	}

}
