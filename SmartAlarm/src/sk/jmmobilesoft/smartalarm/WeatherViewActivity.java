package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.log.Logger;
import android.app.Activity;
import android.os.Bundle;

public class WeatherViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.serviceInfo("WeatherViewActivity: started");
		setContentView(R.layout.weather_view_activity);
	}
}
