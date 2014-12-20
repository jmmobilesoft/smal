package sk.jmmobilesoft.smartalarm;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.PagerAdapter;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import sk.jmmobilesoft.smartalarm.service.ClockRepeatService;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private Menu menu;
	private PagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

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
		Logger.serviceInfo("Application started");
		try {
			setContentView(R.layout.tabs_layout);
			intialiseViewPager();
			initialiseTabHost(savedInstanceState);
			if (savedInstanceState != null) {
				mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
			}
			if (!isMyServiceRunning(ClockRepeatService.class)) {
				Logger.serviceInfo("ClockRepeatService started");
				Intent startRepeatingService = new Intent(this,
						ClockRepeatService.class);
				startService(startRepeatingService);
			}
		} catch (Exception e) {
			Logger.logStackTrace(e.getStackTrace());
			Helper.createToast(this, "Sorry something went wrong");
			finish();
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag());
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();
		fragments
				.add(Fragment.instantiate(this, ClockFragment.class.getName()));
		fragments
				.add(Fragment.instantiate(this, TimerFragment.class.getName()));
		fragments.add(Fragment.instantiate(this,
				WeatherFragment.class.getName()));
		this.mPagerAdapter = new PagerAdapter(
				super.getSupportFragmentManager(), fragments);
		//
		this.mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		MainActivity.addTab(this, this.mTabHost,
				this.mTabHost.newTabSpec(ClockFragment.class.getSimpleName())
						.setIndicator("Alarm"), (tabInfo = new TabInfo(
						ClockFragment.class.getSimpleName(),
						ClockFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity.addTab(this, this.mTabHost,
				this.mTabHost.newTabSpec(TimerFragment.class.getSimpleName())
						.setIndicator("Timer"), (tabInfo = new TabInfo(
						TimerFragment.class.getSimpleName(),
						TimerFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity.addTab(this, this.mTabHost,
				this.mTabHost.newTabSpec(WeatherFragment.class.getSimpleName())
						.setIndicator("Weather"), (tabInfo = new TabInfo(
						WeatherFragment.class.getSimpleName(),
						WeatherFragment.class, args)));
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
		tabHost.addTab(tabSpec);
	}

	public void onTabChanged(String tag) {
		setMenuLabel(tag);
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
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
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_add_action) {
			switch (mTabHost.getCurrentTabTag()) {
			case "ClockFragment": {
				Intent intentA = new Intent(this, ClockViewActivity.class);
				intentA.putExtra("id", -1l);
				startActivity(intentA);
				break;
			}
			case "TimerFragment": {
				Intent intent = new Intent(this, TimerViewActivity.class);
				intent.putExtra("id", -1l);
				startActivity(intent);
				break;
			}
			case "WeatherFragment": {
				WeatherNetworkService nS = new WeatherNetworkService();
				nS.refreshAllWeather(getApplicationContext());
				recreate();
				break;
			}
			}
		}
		if (item.getItemId() == R.id.menu_remove_action) {
			switch (mTabHost.getCurrentTabTag()) {
			case "ClockFragment": {
				Intent intent = new Intent(this, ClockRemoveActivity.class);
				intent.putExtra("id", 0);
				startActivity(intent);
				break;
			}
			case "TimerFragment": {
				// TODO remove activity
				break;
			}
			case "WeatherFragment": {
				Intent intent = new Intent(this, WeatherRemoveActivity.class);
				startActivity(intent);
				break;
			}
			}
		}
		if (item.getItemId() == R.id.menu_settings_action) {
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra("id", 0);
			startActivityForResult(intent, 13);
		}
		Logger.appInfo("Add tab item with value:" + item + " tab:" + mTabHost.getCurrentTabTag());

		return super.onOptionsItemSelected(item);
	}

	private void setMenuLabel(String tag) {
		System.out.println("tag:" + tag);
		try {
			MenuItem item = menu.findItem(R.id.menu_add_action);
			if (tag.equals("WeatherFragment")) {
				item.setTitle("Refresh");
				item.setIcon(getResources().getDrawable(
						R.drawable.ic_action_refresh));
			} else {
				item.setTitle("Add");
				item.setIcon(getResources().getDrawable(
						R.drawable.ic_action_new));
			}
		} catch (Exception e) {
			Logger.logStackTrace(e.getStackTrace());
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		this.mTabHost.setCurrentTab(position);

	}
}
