package sk.jmmobilesoft.smartalarm;

import java.util.Calendar;

import sk.jmmobilesoft.smartalarm.network.NetworkService;
import sk.jmmobilesoft.smartalarm.service.Helper;
import sk.jmmobilesoft.smartalarm.weather.WeatherHttpClient;
import sk.jmmobilesoft.smartalarm.weather.WeatherJsonParser;
import sk.jmmobilesoft.smartalarm.weather.model.WeatherForecast;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SleepScreenFragment extends Fragment {

	private EditText city;
	private TextView weather;
	private NetworkService network;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		network = new NetworkService();
		final View view = inflater.inflate(R.layout.sleep_screen_fragment,
				container, false);

		weather = (TextView) view.findViewById(R.id.weather_text);

		Button wifion = (Button) view.findViewById(R.id.image_button_wifi_on);
		wifion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnWifiOn(getActivity());

			}
		});
		Button wifioff = (Button) view.findViewById(R.id.image_button_wifi_off);
		wifioff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnWifiOff(getActivity());

			}
		});
		Button mobileon = (Button) view
				.findViewById(R.id.image_button_mobile_on);
		mobileon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnMobileOn(getActivity());

			}
		});
		Button mobileoff = (Button) view
				.findViewById(R.id.image_button_mobile_off);
		mobileoff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnMobileOff(getActivity());

			}
		});
		city = (EditText) view.findViewById(R.id.image_edittext_city);
		Button weatherRefresh = (Button) view
				.findViewById(R.id.image_button_weather_refresh);
		weatherRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnWifiOn(getActivity());
				connect();
				weather.setText(getWeatherString());
				network.turnWifiOff(getActivity());
			}
		});

		return view;
	}

	private void connect() {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					int counter = 0;
					while (!network.isConnected(getActivity()) || counter >= 60) {
						Thread.sleep(1000);
						System.out.println("waiting");
						counter++;
					}

				} catch (Exception e) {
					System.out.println(e);
					network.turnWifiOff(getActivity());
				}
			}
		};
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getWeatherString() {
		WeatherHttpClient client = new WeatherHttpClient();
		WeatherJsonParser parser = new WeatherJsonParser();
		WeatherForecast weather = null;
		try {
			client.getWeatherData(city.getText().toString());
			weather = parser.parseData(client.getDataString());
		} catch (NullPointerException e) {
			System.out.println(e);
			return "cant download weather";
		}
		System.out.println("weather:" + weather);

		Calendar sunrise = Calendar.getInstance();
		sunrise.set(Calendar.MILLISECOND, (int) weather.getSunrise());
		String rise = sunrise.get(Calendar.HOUR_OF_DAY) + ":"
				+ sunrise.get(Calendar.MINUTE);

		Calendar sunset = Calendar.getInstance();
		sunset.set(Calendar.MILLISECOND, (int) weather.getSunset());
		String set = sunset.get(Calendar.HOUR_OF_DAY) + ":"
				+ sunset.get(Calendar.MINUTE);

		String fReturn = new String("city:          " + weather.getCityName()
				+ " \ntemp:          "
				+ Helper.kelvinToCelsius(weather.getTemperature())
				+ " \ndescription:   " + weather.getMainDesc()
				+ " \nhumidity:      " + weather.getHumidity()
				+ " \nsunrise:       " + rise + " \nsunset:        " + set);
		return fReturn;
	}
}
