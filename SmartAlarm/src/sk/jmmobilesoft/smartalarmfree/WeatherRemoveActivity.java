package sk.jmmobilesoft.smartalarmfree;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarmfree.R;
import sk.jmmobilesoft.smartalarmfree.database.DBHelper;
import sk.jmmobilesoft.smartalarmfree.helpers.GlobalHelper;
import sk.jmmobilesoft.smartalarmfree.log.Logger;
import sk.jmmobilesoft.smartalarmfree.model.WeatherForecast;
import sk.jmmobilesoft.smartalarmfree.model.WeatherRemoveAdapter;
import sk.jmmobilesoft.smartalarmfree.model.WeatherSelectAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class WeatherRemoveActivity extends Activity {

	private DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.serviceInfo("WeatherRemoveActivity: started");
		GlobalHelper.hideActionBar(this);
		setContentView(R.layout.weather_select_activity);
		db = new DBHelper(this);
		ListView list = (ListView) findViewById(R.id.weather_select_listview);
		List<WeatherForecast> weatherList = null;
		try {
			weatherList = db.getWeatherForecast();
		} catch (IllegalStateException e) {
			Log.i("INFO", "clock databse is empty");
		}
		if (weatherList == null) {
			weatherList = new ArrayList<WeatherForecast>();
		}
		WeatherRemoveAdapter adapter = new WeatherRemoveAdapter(this,
				weatherList, savedInstanceState);
		list.setAdapter(adapter);
		initComponents(adapter);
	}

	private void initComponents(final WeatherRemoveAdapter adapter) {
		Button cancel = (Button) findViewById(R.id.weather_select_activity_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Button delete = (Button) findViewById(R.id.weather_select_activity_save);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean[] checkboxes = adapter.getCheckboxes();
				for (int i = 0; i < adapter.getCount(); i++) {
					if (checkboxes[i]) {
						db.deleteWeatherForecast(adapter.getItemId(i));
					}
				}
				finish();
			}
		});
	}

}
