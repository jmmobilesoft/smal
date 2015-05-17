package sk.jmmobilesoft.smartalarmfree.model;

import java.util.List;

import sk.jmmobilesoft.smartalarmfree.R;
import sk.jmmobilesoft.smartalarmfree.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarmfree.helpers.WeatherHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class WeatherRemoveAdapter extends BaseAdapter {

	private List<WeatherForecast> weathers;
	private Activity context;
	private boolean[] checkboxes;

	public WeatherRemoveAdapter(Activity context,
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
					R.layout.weather_remove_adapter_item, null);
		}
		WeatherForecast weather = weathers.get(position);

		TextView city = (TextView) convertView
				.findViewById(R.id.weather_remove_item_city);
		TextView temp = (TextView) convertView
				.findViewById(R.id.weather_remove_item_temp);
		TextView description = (TextView) convertView
				.findViewById(R.id.weather_remove_item_description);
		TextView maxMinTemp = (TextView) convertView
				.findViewById(R.id.weather_remove_item_maxmintemp);
		final CheckBox delete = (CheckBox) convertView
				.findViewById(R.id.weather_remove_item_delete);

		city.setText(weather.getCityName());
		temp.setText(WeatherHelper.getTemperature(context,
				weather.getTemperature()));
		description.setText(weather.getDescription());
		maxMinTemp.setText(WeatherHelper.getTemperature(context,
				weather.getTempMin())
				+ "/"
				+ WeatherHelper.getTemperature(context, weather.getTempMax()));
		GlobalHelper.setCheckboxStyle(delete);
		delete.setChecked(checkboxes[position]);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				delete.setChecked(!delete.isChecked());
				checkboxes[position] = delete.isChecked();
			}
		});

		return convertView;
	}
	
	public boolean[] getCheckboxes() {
		return checkboxes;
	}
	
}
