package sk.jmmobilesoft.smartalarmfree;

import sk.jmmobilesoft.smartalarmfree.R;
import sk.jmmobilesoft.smartalarmfree.database.DBHelper;
import sk.jmmobilesoft.smartalarmfree.helpers.Helper;
import sk.jmmobilesoft.smartalarmfree.helpers.TimerHelper;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import sk.jmmobilesoft.smartalarmfree.model.Timer;
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

public class TimerRingActivity extends Activity {

	private DBHelper db;

	private MediaPlayer mp;

	private AudioManager mAudioManager;

	private Vibrator v;

	private int originalVolume;

	private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.serviceInfo("TimerRingScreen activity started");
		Helper.wakeLockOn(this); //TODO test

		final Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

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
		TextView name = (TextView) findViewById(R.id.timer_ring_activity_Title);

		String nameS;
		if (t.getName() == null) {
			nameS = "";
		} else
			nameS = t.getName();
		name.setText("Timer " + nameS + " ended.");
		TextView time = (TextView) findViewById(R.id.timer_ring_activity_time);
		time.setText(Helper.format(t.getHours()) + ":"
				+ Helper.format(t.getMinutes()) + ":"
				+ Helper.format(t.getSeconds()));
		TextView start = (TextView) findViewById(R.id.timer_ring_activity_start);
		TextView end = (TextView) findViewById(R.id.timer_ring_activity_end);

		TimerHelper.setTimerAdapterLabels(t, true, start, end);

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
