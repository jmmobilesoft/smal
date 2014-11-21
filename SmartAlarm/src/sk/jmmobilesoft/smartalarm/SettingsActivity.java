package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.network.NetworkService;
import sk.jmmobilesoft.smartalarm.service.Helper;
import sk.jmmobilesoft.smartalarm.weather.WeatherHttpClient;
import sk.jmmobilesoft.smartalarm.weather.WeatherJsonParser;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	private EditText city;
	private TextView weather;
	private NetworkService network;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		network = new NetworkService();
		setContentView(R.layout.settings_activity);

		weather = (TextView) findViewById(R.id.weather_text);

		Button wifion = (Button) findViewById(R.id.image_button_wifi_on);
		wifion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnWifiOn(getApplicationContext());

			}
		});
		Button wifioff = (Button) findViewById(R.id.image_button_wifi_off);
		wifioff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnWifiOff(getApplicationContext());

			}
		});
		Button mobileon = (Button) findViewById(R.id.image_button_mobile_on);
		mobileon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnMobileOn(getApplicationContext());

			}
		});
		Button mobileoff = (Button) findViewById(R.id.image_button_mobile_off);
		mobileoff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				network.turnMobileOff(getApplicationContext());

			}
		});
		city = (EditText) findViewById(R.id.image_edittext_city);
		Button weatherRefresh = (Button) findViewById(R.id.image_button_weather_refresh);
		weatherRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// network.turnWifiOn(getApplicationContext());
				// new WeatherNetworkService().connect(getApplicationContext());
				// weather.setText(getWeatherString());
				// network.turnWifiOff(getApplicationContext());
				new Connect(getApplicationContext()).execute();
			}
		});

		super.onCreate(savedInstanceState);
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
		System.out.println(weather.getSunrise() + "," + weather.getSunset());

		String fReturn = new String("city:          " + weather.getCityName()
				+ " \ntemp:          "
				+ Helper.kelvinToCelsius(weather.getTemperature())
				+ " \ndescription:   " + weather.getMainDesc()
				+ " \nhumidity:      " + weather.getHumidity()
				+ " \nsunrise:       "
				+ Helper.milisToTime(weather.getSunrise())
				+ " \nsunset:        "
				+ Helper.milisToTime(weather.getSunset()));
		return fReturn;
	}

	public class Connect extends AsyncTask<Void, Void, Void> {

		private Context mContext;

		public Connect(Context context) {
			mContext = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				network.turnWifiOn(getApplicationContext());
				int counter = 0;
				while (!network.isConnected(mContext) && counter <= 60) {
					counter++;
					Thread.sleep(1000);
					System.out.println("waiting:" + counter);
				}

			} catch (Exception e) {
				System.out.println(e);
				// network.turnWifiOff(mContext);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (network.isConnected(mContext)) {
				weather.setText(getWeatherString());
			}
			network.turnWifiOff(getApplicationContext());
			super.onPostExecute(result);
		}
	}

}
