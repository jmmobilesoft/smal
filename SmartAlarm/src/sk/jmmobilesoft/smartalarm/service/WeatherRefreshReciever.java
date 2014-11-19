package sk.jmmobilesoft.smartalarm.service;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class WeatherRefreshReciever extends BroadcastReceiver {

	private NetworkService network;
	
	private Long id;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("INFO", "WeatherRefreshReciever: onRecieve");
		intent.getLongExtra("ID", 0l);
		network = new NetworkService();
		new Connect(context).execute();
	}
	
	public class Connect extends AsyncTask<Void, Void, Void> {

		private Context mContext;
		
		public Connect(Context context){
			mContext = context;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				network.turnWifiOn(mContext);
				int counter = 0;
				while (!network.isConnected(mContext) || counter >= 60) {
					Thread.sleep(1000);
					System.out.println("waiting");
					counter++;
				}

			} catch (Exception e) {
				System.out.println(e);
				//network.turnWifiOff(mContext);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			DBHelper db = new DBHelper(mContext);
			Clock c = db.getClock(id);
			WeatherNetworkService service = new WeatherNetworkService();
			List<String> cityList = new ArrayList<>();// c.getCities();
			for (WeatherForecast w : db.getWeather()) {
				cityList.add(w.getCityName());
			}
			List<WeatherForecast> weathers = service.getWeather(cityList);
			if (weathers != null) {
				for (WeatherForecast w : weathers) {
					WeatherForecast up = db.getWeatherByCity(w.getCityName());
					if (up != null) {
						w.setId(up.getId());
						db.updateWeather(w);
					} else {
						db.createWeather(w);
					}
				}
			}
			network.turnWifiOff(mContext);
			super.onPostExecute(result);
		}
	}

}
