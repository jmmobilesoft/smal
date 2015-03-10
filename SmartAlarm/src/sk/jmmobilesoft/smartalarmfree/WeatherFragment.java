package sk.jmmobilesoft.smartalarmfree;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarmfree.R;
import sk.jmmobilesoft.smartalarmfree.database.DBHelper;
import sk.jmmobilesoft.smartalarmfree.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarmfree.helpers.Helper;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import sk.jmmobilesoft.smartalarmfree.model.Weather;
import sk.jmmobilesoft.smartalarmfree.model.WeatherAdapter;
import sk.jmmobilesoft.smartalarmfree.model.WeatherForecast;
import sk.jmmobilesoft.smartalarmfree.network.NetworkService;
import sk.jmmobilesoft.smartalarmfree.network.WeatherNetworkService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WeatherFragment extends Fragment {

	private List<WeatherForecast> weathers;
	private WeatherAdapter adapter;
	private DBHelper db;
	private ListView list;
	private Bundle state;
	private EditText city;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.weather_fragment,
				container, false);
		try {
			state = savedInstanceState;
			db = new DBHelper(getActivity());
			list = (ListView) view.findViewById(R.id.sleep_screen_listview);

			try {
				weathers = db.getWeatherForecast();
			} catch (IllegalStateException e) {
				Logger.logStackTrace(e.getStackTrace());
			}
			if (weathers == null) {
				weathers = new ArrayList<WeatherForecast>();
			}
			adapter = new WeatherAdapter(this, weathers, savedInstanceState);
			list.setAdapter(adapter);
			city = (EditText) view.findViewById(R.id.sleep_screen_city);
			Button add = (Button) view.findViewById(R.id.sleep_screen_add);
			add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						new Refresh().execute();
					} catch (Exception e) {
						Logger.logStackTrace(e.getStackTrace());
						Helper.createToast(getActivity(),
								"Error ocured, please try again later");
					}
				}
			});
		} catch (Exception e) {
			Logger.logStackTrace(e.getStackTrace());
		}
		return view;
	}

	@Override
	public void onResume() {
		refreshWeathers();
		super.onPause();
	}

	private void refreshWeathers() {
		weathers = db.getWeatherForecast();
		if (weathers == null) {
			weathers = new ArrayList<>();
		}
		adapter = new WeatherAdapter(this, weathers, state);
		list.setAdapter(adapter);
	}

	class Refresh extends AsyncTask<Void, Void, Void> {

		private boolean toastNotFound = false;
		private boolean toastNoNetwork = false;
		private String cityS;

		@Override
		protected Void doInBackground(Void... params) {
			WeatherNetworkService service = new WeatherNetworkService();
			if (new NetworkService().isConnected(getActivity())) {
				cityS = city.getText().toString();
				if (service.availableWeather(cityS)) {
					List<String> cities = new ArrayList<String>();
					cities.add(cityS);
					List<WeatherForecast> w = service
							.downloadWeatherForecast(cities);
					WeatherForecast weather = w.get(0);
					if (db.getWeatherForecastByCity(weather.getCityName()) == null) { // TODO
						// CHECK
						db.createWeatherForecast(weather);
					} else {
						weather.setId(db.getWeatherForecastByCity(
								weather.getCityName()).getId());
						db.updateWeatherForecast(weather);
					}
					List<Weather> weather2 = service.downloadWeather(
							getActivity(), cities);
					if (weathers != null) {
						db.deleteWeatherByCity(cityS);
						for (Weather w2 : weather2) {
							db.createWeather(w2);
						}
					}
				} else {
					toastNotFound = true;

				}
			} else {
				toastNoNetwork = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			refreshWeathers();
			city.setText("");
			if (toastNoNetwork) {
				String text = "Network connection unavailable.";
				Helper.createToast(getActivity(), text);
			}
			if (toastNotFound) {
				String text = "Weather for city " + cityS + " was not found.";
				Helper.createToast(getActivity(), text);
			}
			super.onPostExecute(result);
		}

	}
}
