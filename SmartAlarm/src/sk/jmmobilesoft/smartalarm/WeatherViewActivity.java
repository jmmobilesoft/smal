package sk.jmmobilesoft.smartalarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Weather;
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
		DBHelper db = new DBHelper(this);
		setView(db.getWeatherForecast(id), db);
	}

	private void setView(WeatherForecast weatherForecast, DBHelper db) {
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

		int resourceId = getResources().getIdentifier(
				"w" + weatherForecast.getIcon(), "drawable", getPackageName());
		main.setImageDrawable(getResources().getDrawable(resourceId));
		city.setText(weatherForecast.getCityName());
		temp.setText(Helper.kelvinToCelsius(weatherForecast.getTemperature())
				+ "°C");
		minTemp.setText(Helper.kelvinToCelsius(weatherForecast.getTempMin())
				+ "°C");
		maxTemp.setText(Helper.kelvinToCelsius(weatherForecast.getTempMax())
				+ "°C");
		description.setText(weatherForecast.getDescription());
		sunrise.setText(Helper.milisToTime(weatherForecast.getSunrise()));
		sunset.setText(Helper.milisToTime(weatherForecast.getSunset()));
		humidity.setText(weatherForecast.getHumidity() + " %");
		pressure.setText(weatherForecast.getPressure() + " hPa");
		winddeg.setText(weatherForecast.getWindDeg() + " deg");
		windspeed.setText(weatherForecast.getWindSpeed() + " m/s");
		update.setText(weatherForecast.getUpdateTime());

		SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat useFromat = new SimpleDateFormat("EEE dd");

		TextView dated1 = (TextView) findViewById(R.id.weather_view_day1_date);
		ImageView imageViewd1 = (ImageView) findViewById(R.id.weather_view_day1_imageView);
		TextView tempd1 = (TextView) findViewById(R.id.weather_view_day1_temp);
		TextView descd1 = (TextView) findViewById(R.id.weather_view_day1_description);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);

		TextView dated2 = (TextView) findViewById(R.id.weather_view_day2_date);
		ImageView imageViewd2 = (ImageView) findViewById(R.id.weather_view_day2_imageView);
		TextView tempd2 = (TextView) findViewById(R.id.weather_view_day2_temp);
		TextView descd2 = (TextView) findViewById(R.id.weather_view_day2_description);
		Calendar c2 = Calendar.getInstance();
		c2.add(Calendar.DAY_OF_MONTH, 2);

		TextView dated3 = (TextView) findViewById(R.id.weather_view_day3_date);
		ImageView imageViewd3 = (ImageView) findViewById(R.id.weather_view_day3_imageView);
		TextView tempd3 = (TextView) findViewById(R.id.weather_view_day3_temp);
		TextView descd3 = (TextView) findViewById(R.id.weather_view_day3_description);
		Calendar c3 = Calendar.getInstance();
		c3.add(Calendar.DAY_OF_MONTH, 3);

		try {
			Weather day1 = db
					.getWeatherByCityDate(weatherForecast.getCityName(),
							formater.format(c.getTime()));
			dated1.setText(useFromat.format(c.getTime()));
			int resourceDay1 = getResources().getIdentifier(
					"w" + day1.getIcon(), "drawable", getPackageName());
			imageViewd1.setImageDrawable(getResources().getDrawable(
					resourceDay1));
			tempd1.setText(day1.getTempMin() + "/" + day1.getTempMax() + " °C");
			descd1.setText(day1.getDescription());
		} catch (RuntimeException e) {
			dated1.setText(useFromat.format(c.getTime()));
			imageViewd1.setImageDrawable(getResources().getDrawable(
					R.drawable.refresh));
			tempd1.setText("No data");
			descd1.setText("refresh");
		}

		try {
			Weather day2 = db.getWeatherByCityDate(
					weatherForecast.getCityName(),
					formater.format(c2.getTime()));
			dated2.setText(useFromat.format(c2.getTime()));
			int resourceDay2 = getResources().getIdentifier(
					"w" + day2.getIcon(), "drawable", getPackageName());
			imageViewd2.setImageDrawable(getResources().getDrawable(
					resourceDay2));
			tempd2.setText(day2.getTempMin() + "/" + day2.getTempMax() + " °C");
			descd2.setText(day2.getDescription());
		} catch (RuntimeException e) {
			dated2.setText(useFromat.format(c2.getTime()));
			imageViewd2.setImageDrawable(getResources().getDrawable(
					R.drawable.refresh));
			tempd2.setText("No data");
			descd2.setText("refresh");
		}

		try {
			Weather day3 = db.getWeatherByCityDate(
					weatherForecast.getCityName(),
					formater.format(c3.getTime()));
			dated3.setText(useFromat.format(c3.getTime()));
			int resourceDay3 = getResources().getIdentifier(
					"w" + day3.getIcon(), "drawable", getPackageName());
			imageViewd3.setImageDrawable(getResources().getDrawable(
					resourceDay3));
			tempd3.setText(day3.getTempMin() + "/" + day3.getTempMax() + " °C");
			descd3.setText(day3.getDescription());
		} catch (RuntimeException e) {
			dated3.setText(useFromat.format(c3.getTime()));
			imageViewd3.setImageDrawable(getResources().getDrawable(
					R.drawable.refresh));
			tempd3.setText("No data");
			descd3.setText("refresh");
		}
	}

	private WeatherForecast getDatasFromDB(Long id) {
		DBHelper db = new DBHelper(this);
		return db.getWeatherForecast(id);
	}
}
