package sk.jmmobilesoft.smartalarm;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class MainActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener {

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;

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
						.setIndicator("Tab 1"), (tabInfo = new TabInfo("Tab1",
						ClockAlarmFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity
				.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2")
						.setIndicator("Tab 2"), (tabInfo = new TabInfo("Tab2",
						TimerAlarmFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity
				.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3")
						.setIndicator("Tab 3"), (tabInfo = new TabInfo("Tab3",
						SleepScreenFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		MainActivity
				.addTab(this,
						this.mTabHost,
						this.mTabHost.newTabSpec("Settings").setIndicator(
								"Settings"), (tabInfo = new TabInfo("Settings",
								SettingsFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		this.onTabChanged("Tab1");
		//
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
	}
}
