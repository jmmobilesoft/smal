package sk.jmmobilesoft.smartalarm;

import java.util.ArrayList;
import java.util.List;

import sk.jmmobilesoft.smartalarm.database.DBHelper;
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
import android.widget.Toast;

public class WeatherSelectActivity extends Activity {

	private DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
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
		WeatherSelectAdapter adapter = new WeatherSelectAdapter(this, weatherList,
				savedInstanceState);
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
		Button select = (Button) findViewById(R.id.weather_select_activity_save);
		select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int counter = 0;
				List<Long> w = new ArrayList<>();
				boolean[] checkboxes = adapter.getCheckboxes();
				for(int i = 0; i< adapter.getCount(); i++){
					if(checkboxes[i]){
						w.add(adapter.getItemId(i));
						counter++;
					}
				}
				if(counter <= 2){
					ArrayList<Integer> r = new ArrayList<Integer>();
					for(int i = 0; i < w.size(); i++){
						r.add(w.get(i).intValue());
					}
					Bundle b = new Bundle();
					b.putIntegerArrayList("weathers", r);
					Intent result = new Intent();
					result.putExtra("result", b);
					setResult(RESULT_OK, result);
					finish();
				}else{
					Toast t = Toast.makeText(getApplicationContext(), "Maximum cities to display for alarm are 2.", Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});
	}
}
