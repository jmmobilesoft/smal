package sk.jmmobilesoft.smartalarm.model;

import java.util.List;

import sk.jmmobilesoft.smartalarm.R;
import sk.jmmobilesoft.smartalarm.helpers.Helper;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class WeatherSelectAdapter extends BaseAdapter {	
	
	private List<WeatherForecast> weathers;
	private Activity context;
	private boolean[] checkboxes;

	public WeatherSelectAdapter(Activity context,
			List<WeatherForecast> weathers, Bundle state) {
		super();
		this.context = context;
		this.weathers = weathers;
		checkboxes = new boolean[getCount()];
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
		final LayoutInflater inflater = context.getLayoutInflater();
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.weather_select_adapter_item, null);
		}
		System.out.println(weathers);
		System.out.println("position:" + weathers.get(position));
		WeatherForecast weather = weathers.get(position);

		TextView city = (TextView) convertView
				.findViewById(R.id.weather_select_item_city);
		TextView temp = (TextView) convertView
				.findViewById(R.id.weather_select_item_temp);
		TextView description = (TextView) convertView
				.findViewById(R.id.weather_select_item_description);
		TextView maxMinTemp = (TextView) convertView
				.findViewById(R.id.weather_select_item_maxmintemp);
		CheckBox selector = (CheckBox) convertView
				.findViewById(R.id.weathe_select_item_selector);

		city.setText(weather.getCityName());
		temp.setText(Helper.kelvinToCelsius(weather.getTemperature()) + "°"
				+ "C"); // TODO jednotka teploty z nastaveni
		description.setText(weather.getDescription());
		maxMinTemp.setText(Helper.kelvinToCelsius(weather.getTempMin()) + "/"
				+ Helper.kelvinToCelsius(weather.getTempMax()) + "°" + "C");
		selector.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				checkboxes[position] = isChecked;
			}
		});

		return convertView;
	}
	
	public boolean[] getCheckboxes(){
		return checkboxes;
	}

}
