package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.WeatherAdapter;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import sk.jmmobilesoft.smartalarm.network.WeatherNetworkService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SleepScreenFragment extends Fragment {

	private List<WeatherForecast> weathers;
	private WeatherAdapter adapter;
	private DBHelper db;
	private ListView list;
	private Bundle state;
	private Fragment context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.sleep_screen_fragment,
				container, false);
		state = savedInstanceState;
		db = new DBHelper(getActivity());
		list = (ListView) view.findViewById(R.id.sleep_screen_listview);

		try {
			weathers = db.getWeather();
		} catch (IllegalStateException e) {
			Log.i("INFO", "clock databse is empty");
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
						List<WeatherForecast> w = service.downloadWeather(
								cities, getActivity());
						WeatherForecast weather = w.get(0);
						if (db.getWeatherByCity(weather.getCityName()) == null) { // TODO
																					// CHECK
							db.createWeather(weather);
							refreshWeathers();
							city.setText("");
						} else {
							weather.setId(db.getWeatherByCity(
									weather.getCityName()).getId());
							refreshWeathers();
							city.setText("");
						}
					} else {
						String text = "Weather for city "
								+ city.getText().toString() + " was not found.";
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
