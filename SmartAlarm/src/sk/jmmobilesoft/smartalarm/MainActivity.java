package sk.jmmobilesoft.smartalarm;

import java.util.HashMap;

import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.service.ClockRepeatService;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener {

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;
	private String activeTab;

	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_layout);
		initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		if (!isMyServiceRunning(ClockRepeatService.class)) {
			Log.i("INFO", "ClockRepeatService started");
			Intent startRepeatingService = new Intent(this,
					ClockRepeatService.class);
			startService(startRepeatingService);
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag());
		super.onSaveInstanceState(outState);
	}

	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		MainActivity
				.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1")
						.setIndicator("Alarm"), (tabInfo = new TabInfo("Tab1",
						ClockAlarmFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity
				.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2")
						.setIndicator("Timer"), (tabInfo = new TabInfo("Tab2",
						TimerAlarmFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity
				.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3")
						.setIndicator("Weather"), (tabInfo = new TabInfo("Tab3",
						SleepScreenFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		this.onTabChanged("Tab1");
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(Color.parseColor("#F7F5F5"));
		}
		mTabHost.setOnTabChangedListener(this);
	}

	private static void addTab(MainActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();

		tabInfo.fragment = activity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}

		tabHost.addTab(tabSpec);
	}

	public void onTabChanged(String tag) {
		TabInfo newTab = this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getSupportFragmentManager()
					.beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this,
							newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}

			mLastTab = newTab;
			ft.commit();
			this.getSupportFragmentManager().executePendingTransactions();
		}
		activeTab = newTab.fragment.getClass().getSimpleName();
	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_add_action) {
			switch (activeTab) {
			case "ClockAlarmFragment": {
				Intent intentA = new Intent(this, ClockViewActivity.class);
				intentA.putExtra("id", 0);
				startActivityForResult(intentA, 10);
				break;
			}
			case "TimerAlarmFragment": {
				Intent intent = new Intent(this, TimerViewActivity.class);
				intent.putExtra("id", 0);
				startActivityForResult(intent, 11);
				break;
			}
			case "SleepScreenFragment": {
				break;
			}
			case "SettingsFragment": {
				break;
			}
			}
		}
		if (item.getItemId() == R.id.menu_remove_action) {
			switch (activeTab) {
			case "ClockAlarmFragment": {
				Intent intent = new Intent(this, ClockRemoveActivity.class);
				intent.putExtra("id", 0);
				startActivityForResult(intent, 12);
				break;
			}
			case "TimerAlarmFragment": {
				// TODO remove activity
				break;
			}
			}
		}
		if(item.getItemId() == R.id.menu_settings_action) {
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra("id", 0);
			startActivityForResult(intent, 13);
		}
		Logger.appInfo("Add tab item with value:" + item);

		return super.onOptionsItemSelected(item);
	}
}
