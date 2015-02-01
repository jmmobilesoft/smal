package sk.jmmobilesoft.smartalarm.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Weather;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;

public class WeatherRefreshService extends Service {

	private NetworkService network;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.serviceInfo("WeatherRefreshService: onStartCommand");
		network = new NetworkService();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Calendar cU = Calendar.getInstance();
		cU.setTimeInMillis(sp.getLong("update", 0l));
		Calendar cC = Calendar.getInstance();
		cC.add(Calendar.HOUR_OF_DAY, 1);
		if(cU.getTimeInMillis() != 0 && cU.before(cC)){
			new Connect(getApplicationContext()).execute();
		}
		else{
			Logger.serviceInfo("Last refresh:" + cU.getTime() + " refresh stopped");
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Logger.serviceInfo("WeatherRefreshService: onDestroy");
		super.onDestroy();
	}

	class Connect extends AsyncTask<Void, Void, Void> {

		private Context mContext;

		private WifiLock wifiLock;

		private WakeLock lock;

		private boolean wifiOn = false;

		private boolean mobileOn = false;

		private boolean useMobile = false;

		public Connect(Context context) {
			mContext = context;
			wifiOn = network.wifiAllowed(context);
			mobileOn = network.mobileAllowed(context);
		}

		@Override
		protected Void doInBackground(Void... params) {
			PowerManager pm = (PowerManager) getApplicationContext()
					.getSystemService(Context.POWER_SERVICE);
			lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					"WeatherRefresh");
			lock.acquire();
			Logger.serviceInfo("Wake lock acquired");

			useMobile = GlobalHelper.getBooleanPreference(mContext,
					"allow_mobile", false);
			if (useMobile && !wifiOn) {
				network.turnMobileOn(mContext);
				Logger.serviceInfo("Mobile data turned on");
			} else {
				WifiManager wManager = (WifiManager) getApplicationContext()
						.getSystemService(Context.WIFI_SERVICE);
				if (android.os.Build.VERSION.SDK_INT > 12)
					wifiLock = wManager
							.createWifiLock(
									WifiManager.WIFI_MODE_FULL_HIGH_PERF,
									"RefreshLock");
				else
					wifiLock = wManager.createWifiLock(
							WifiManager.WIFI_MODE_FULL, "RefreshLock");
				wifiLock.acquire();
				Logger.serviceInfo("Wifi lock acquired");
				wManager.setWifiEnabled(true);
				Logger.serviceInfo("scan start:" + wManager.startScan());
				Logger.serviceInfo("Default parametres = wifi:" + wifiOn
						+ " mobile:" + mobileOn + " useMobile:" + useMobile);
			}
			try {
				int counter = 0;
				while (!network.isConnected(mContext) && counter <= 40) {
					counter++;
					Logger.serviceInfo("counter:" + counter);
					Thread.sleep(3000);
				}
				Logger.serviceInfo("Network after 120 seconds connected: "
						+ network.isConnected(mContext));

			} catch (Exception e) {
				Logger.logStackTrace(e.getStackTrace());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (network.isConnected(mContext)) {
				DBHelper db = new DBHelper(mContext);
				WeatherNetworkService service = new WeatherNetworkService();
				List<String> cityList = new ArrayList<>();// c.getCities();
				for (WeatherForecast w : db.getWeatherForecast()) {
					cityList.add(w.getCityName());
				}
				List<WeatherForecast> weathers = service
						.downloadWeatherForecast(cityList);
				if (weathers != null) {
					for (WeatherForecast w : weathers) {
						WeatherForecast up = db.getWeatherForecastByCity(w
								.getCityName());
						if (up != null) {
							w.setId(up.getId());
							db.updateWeatherForecast(w);
						} else {
							db.createWeatherForecast(w);
						}
					}
				}
				List<Weather> weather = service.downloadWeather(mContext, cityList);
				if (weathers != null) {
					db.deleteAllWeather();
					for (Weather w : weather) {
						db.createWeather(w);
					}
				}
			}
			Logger.serviceInfo("Network connected: "
					+ network.isConnected(mContext));
			if (!wifiOn) {
				network.turnWifiOff(mContext);
				Logger.serviceInfo("Wifi turned off");
			}
			if (useMobile && !mobileOn) {
				network.turnMobileOff(mContext);
				Logger.serviceInfo("Mobile turned off");
			}
			lock.release();
			wifiLock.release();
			Logger.serviceInfo("locks released");
			stopSelf();
		}
	}
}
