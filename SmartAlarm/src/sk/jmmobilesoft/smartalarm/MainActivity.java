package sk.jmmobilesoft.smartalarm;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		TabHost host = getTabHost();
		
		Intent clockIntent = new Intent(this,ClockActivity.class);
		TabSpec clock = host.newTabSpec("clock");
		clock.setIndicator("clock");	
		clock.setContent(clockIntent);
		host.addTab(clock);
		
		Intent timerIntent = new Intent(this,TimerActivity.class);
		TabSpec timer = host.newTabSpec("timer");
		timer.setIndicator("timer");
		timer.setContent(timerIntent);		
		host.addTab(timer);
		
		Intent sleepIntent = new Intent(this,SleepActivity.class);
		TabSpec sleep = host.newTabSpec("sleep");
		sleep.setIndicator("sleep");
		sleep.setContent(sleepIntent);
		host.addTab(sleep);
		
//		FragmentManager fm = getFragmentManager();
//		Fragment clockFragment = new ClockAlarmFragment();
//		FragmentTransaction transaction = fm.beginTransaction();
//		transaction.add(R.layout.clock_alarm_fragment, clockFragment);
//		transaction.commit();
	}	
}
