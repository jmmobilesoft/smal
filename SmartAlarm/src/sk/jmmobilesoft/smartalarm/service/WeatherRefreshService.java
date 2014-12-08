package sk.jmmobilesoft.smartalarm.service;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Weather;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

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
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		WakeLock lock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"WeatherRefresh");
		lock.acquire();
		intent.getLongExtra("ID", 0l);
		network = new NetworkService();
		new Connect(getApplicationContext()).execute();
		lock.release();
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	public class Connect extends AsyncTask<Void, Void, Void> {

		private Context mContext;

		public Connect(Context context) {
			mContext = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				network.turnWifiOn(mContext);
				int counter = 0;
				while (!network.isConnected(mContext) && counter <= 60) {
					counter++;
					Thread.sleep(1000);
				}

			} catch (Exception e) {
				Logger.logStackTrace(e.getStackTrace());
				// network.turnWifiOff(mContext);
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
				List<Weather> weather = service.downloadWeather(cityList);
				if (weathers != null) {
					db.deleteAllWeather();
					for (Weather w : weather) {
						db.createWeather(w);
					}
				}
			}
			Logger.serviceInfo("Network connected: "
					+ network.isConnected(mContext));
			network.turnWifiOff(mContext);
			super.onPostExecute(result);
		}
	}
}
