package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.WeatherAdapter;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class WeatherFragment extends Fragment {

	private List<WeatherForecast> weathers;
	private WeatherAdapter adapter;
	private DBHelper db;
	private ListView list;
	private Bundle state;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			final View view = inflater.inflate(R.layout.weather_fragment,
					container, false);
			state = savedInstanceState;
			db = new DBHelper(getActivity());
			list = (ListView) view.findViewById(R.id.sleep_screen_listview);

			try {
				weathers = db.getWeather();
			} catch (IllegalStateException e) {
				StackTraceElement[] s = e.getStackTrace();
				for (int i = 0; i < s.length; i++) {
					Logger.appInfo(s[i].toString());
				}
			}
			if (weathers == null) {
				weathers = new ArrayList<WeatherForecast>();
			}
			adapter = new WeatherAdapter(this, weathers, savedInstanceState);
			list.setAdapter(adapter);
			final EditText city = (EditText) view
					.findViewById(R.id.sleep_screen_city);
			Button add = (Button) view.findViewById(R.id.sleep_screen_add);
			add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					WeatherNetworkService service = new WeatherNetworkService();
					if (new NetworkService().isConnected(getActivity())) {
						String cityS = city.getText().toString();
						if (service.availableWeather(cityS)) {
							List<String> cities = new ArrayList<String>();
							cities.add(cityS);
							List<WeatherForecast> w = service.getWeather(cities);
							WeatherForecast weather = w.get(0);
							if (db.getWeatherByCity(weather.getCityName()) == null) { // TODO
																						// CHECK
								db.createWeather(weather);
								refreshWeathers();
								city.setText("");
							} else {
								weather.setId(db.getWeatherByCity(
										weather.getCityName()).getId());
								db.updateWeather(weather);
								refreshWeathers();
								city.setText("");
							}
						} else {
							String text = "Weather for city "
									+ city.getText().toString()
									+ " was not found.";
							Toast t = Toast.makeText(getActivity(), text,
									Toast.LENGTH_SHORT);
							t.show();
						}
					} else {
						String text = "Network connection unavailable.";
						Toast t = Toast.makeText(getActivity(), text,
								Toast.LENGTH_SHORT);
						t.show();
					}

				}
			});
			return view;
		} catch (Exception e) {
			StackTraceElement[] s = e.getStackTrace();
			for (int i = 0; i < s.length; i++) {
				Logger.serviceInfo(s[i].toString());
			}
			throw e;
		}
	}

	@Override
	public void onResume() {
		refreshWeathers();
		super.onPause();
	}
	
	private void refreshWeathers() {
		weathers = db.getWeather();
		if (weathers == null) {
			weathers = new ArrayList<>();
		}
		adapter = new WeatherAdapter(this, weathers, state);
		list.setAdapter(adapter);
	}
}
