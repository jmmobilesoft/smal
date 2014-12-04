package sk.jmmobilesoft.smartalarm;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.service.Helper;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.serviceInfo("WeatherViewActivity: started");
		setContentView(R.layout.weather_view_activity);
		Long id = this.getIntent().getExtras().getLong("id");
		setView(getDatasFromDB(id));
	}
	
	private void setView(WeatherForecast weather){
		ImageView main = (ImageView) findViewById(R.id.weather_view_mainImage);
		TextView city = (TextView) findViewById(R.id.weather_view_city_name);
		TextView temp = (TextView) findViewById(R.id.weather_view_temp);
		TextView description = (TextView) findViewById(R.id.weather_view_description);
		TextView minTemp = (TextView) findViewById(R.id.weather_view_mintemp_textview);
		TextView maxTemp = (TextView) findViewById(R.id.weather_view_maxtemp_textview);
		TextView sunrise = (TextView) findViewById(R.id.weather_view_sunrise_textview);
		TextView sunset = (TextView) findViewById(R.id.weather_view_sunset_textview);
		TextView humidity = (TextView) findViewById(R.id.weather_view_humidity_textview);
		TextView pressure = (TextView) findViewById(R.id.weather_view_pressure_textview);
		TextView winddeg = (TextView) findViewById(R.id.weather_view_winddeg_textview);
		TextView windspeed = (TextView) findViewById(R.id.weather_view_windspeed_textview);
		TextView update = (TextView) findViewById(R.id.weather_view_updated);
		
		int resourceId = getResources().getIdentifier("w" + weather.getIcon(),
				"drawable", getPackageName());
		main.setImageDrawable(getResources().getDrawable(resourceId));
		city.setText(weather.getCityName());
		temp.setText(Helper.kelvinToCelsius(weather.getTemperature()) + "°C");
		minTemp.setText(Helper.kelvinToCelsius(weather.getTempMin()) + "°C");
		maxTemp.setText(Helper.kelvinToCelsius(weather.getTempMax()) + "°C");
		description.setText(weather.getDecsription());
		sunrise.setText(Helper.milisToTime(weather.getSunrise()));
		sunset.setText(Helper.milisToTime(weather.getSunset()));
		humidity.setText(weather.getHumidity() + " %");
		pressure.setText(weather.getPressure() + " hPa");
		winddeg.setText(weather.getWindDeg() + " deg");
		windspeed.setText(weather.getWindSpeed() + " m/s");
		update.setText(weather.getUpdateTime());
	}
	
	private WeatherForecast getDatasFromDB(Long id){
		DBHelper db = new  DBHelper(this);
		return db.getWeather(id);
	}
}
