package sk.jmmobilesoft.smartalarm.service;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WeatherRefreshReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("INFO", "WeatherRefreshReciever: onRecieve");
		Long id = (long) intent.getIntExtra("ID", 0);
		DBHelper db = new DBHelper(context);
		WeatherNetworkService service = new WeatherNetworkService();
		List<String> cityList = new ArrayList<String>(); //db.getCitiesForAlarm(id);
		List<WeatherForecast> weathers = service.downloadWeather(cityList,
				context);
		for (WeatherForecast w : weathers) {
			WeatherForecast up = db.getWeatherByCity(w.getCityName());
			if (up != null) {
				w.setId(up.getId());
				db.updateWeather(w);
			}
			else{
				db.createWeather(w);
			}
		}

	}

}
