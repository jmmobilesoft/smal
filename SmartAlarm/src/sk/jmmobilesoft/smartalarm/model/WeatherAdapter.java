package sk.jmmobilesoft.smartalarm.model;

import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.service.Helper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeatherAdapter extends BaseAdapter {

	private List<WeatherForecast> weathers;
	private Fragment context;
	private Bundle savedInstanceState;
	private DBHelper db;

	public WeatherAdapter(Fragment context, List<WeatherForecast> weathers,
			Bundle state) {
		super();
		this.context = context;
		this.weathers = weathers;
		this.savedInstanceState = state;
		db = new DBHelper(context.getActivity());
	}

	@Override
	public int getCount() {
		return weathers.size();
	}

	@Override
	public Object getItem(int position) {
		return weathers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return weathers.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = context
				.getLayoutInflater(savedInstanceState);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.weather_adapter_item, null);
		}
		System.out.println(weathers);
		System.out.println("position:" + weathers.get(position));
		WeatherForecast weather = weathers.get(position);

		TextView city = (TextView) convertView
				.findViewById(R.id.weather_item_city);
		TextView temp = (TextView) convertView
				.findViewById(R.id.weather_item_temp);
		TextView description = (TextView) convertView
				.findViewById(R.id.weather_item_description);
		TextView maxMinTemp = (TextView) convertView
				.findViewById(R.id.weather_item_maxmintemp);

		city.setText(weather.getCityName());
		temp.setText(Helper.kelvinToCelsius(weather.getTemperature()) + "°"
				+ "C"); // TODO jednotka teploty z nastaveni
		description.setText(weather.getDecsription());
		maxMinTemp.setText(Helper.kelvinToCelsius(weather.getTempMin()) + "/"
				+ Helper.kelvinToCelsius(weather.getTempMax()) + "°" + "C");
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				db.deleteClock(getItemId(position));
				// TODO weather detail activity
				// Intent intentA = new Intent(context.getActivity(),
				// ClockViewActivity.class);
				// intentA.putExtra("id", getItemId(position));
				// context.startActivity(intentA);
			}
		});
		return convertView;
	}

}
