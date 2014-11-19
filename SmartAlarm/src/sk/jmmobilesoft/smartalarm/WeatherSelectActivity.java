package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
import sk.jmmobilesoft.smartalarm.model.Clock;
import sk.jmmobilesoft.smartalarm.model.ClockRemoveAdapter;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;
import sk.jmmobilesoft.smartalarm.model.WeatherSelectAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class WeatherSelectActivity extends Activity {

	private DBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_select_activity);
		db = new DBHelper(this);
		ListView list = (ListView) findViewById(R.id.weather_select_listview);
		List<WeatherForecast> weatherList = null;
		try {
			weatherList = db.getWeather();
		} catch (IllegalStateException e) {
			Log.i("INFO", "clock databse is empty");
		}
		if (weatherList == null) {
			weatherList = new ArrayList<WeatherForecast>();
		}
		WeatherSelectAdapter adapter = new WeatherSelectAdapter(this, weatherList, savedInstanceState);
		list.setAdapter(adapter);
		initComponents(adapter);
	}
	
	private void initComponents(final WeatherSelectAdapter adapter){
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
				int counter = 0;
				List<Integer> w = new ArrayList<>();
				for(int i = 0; i< adapter.getCount(); i++){
					if(WeatherSelectAdapter.checkboxes[i]){
						w.add(i);
						counter++;
					}
				}
				if(counter <= 2 && counter > 0){
					int[] r = new int[2];
					for(int i = 0; i < 2; i++){
						r[i] = w.get(i);
					}
					Intent result = new Intent();
					result.putExtra("result", r);
					setResult(111, result);
					finish();
				}
			}
		});
	}
}
