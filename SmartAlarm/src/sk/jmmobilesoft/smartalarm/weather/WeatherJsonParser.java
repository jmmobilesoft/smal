package sk.jmmobilesoft.smartalarm.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.jmmobilesoft.smartalarm.service.Helper;
import sk.jmmobilesoft.smartalarm.weather.model.WeatherForecast;

public class WeatherJsonParser {

	public WeatherForecast parseData(String dataS){
		
		JSONObject data = null;
		try {
			data = new JSONObject(dataS);
		} catch (JSONException e1) {
			// TODO log cant parse
		}
		
		WeatherForecast weather = new WeatherForecast();
		
		try {
			JSONObject corObject = data.getJSONObject("coord");
			weather.setLatitude(corObject.getString("lat"));
			weather.setLongitude(corObject.getString("lon"));
		} catch (JSONException e) {
			//TODO log
		}
		
		
		try {
			JSONObject sysObj = data.getJSONObject("sys");
			weather.setCountry(sysObj.getString("country"));
			weather.setSunrise(sysObj.getLong("sunrise"));
			weather.setSunset(sysObj.getLong("sunset"));			
		} catch (JSONException e) {
			// TODO log
		}

		
		try {
			JSONObject infoObj = data.getJSONObject("main");
			weather.setCityName(data.getString("name"));
			weather.setTemperature((float) infoObj.getDouble("temp"));
			weather.setHumidity(infoObj.getInt("humidity"));
			weather.setPressure(infoObj.getInt("pressure"));
			weather.setTempMin(infoObj.getInt("temp_min"));
			weather.setTempMax(infoObj.getInt("temp_max"));
			
			JSONObject windObj = data.getJSONObject("wind");
			weather.setWindSpeed((float) windObj.getDouble("speed"));
			weather.setWindDeg((float) windObj.getDouble("deg"));
			
			JSONObject cloudsObj = data.getJSONObject("clouds");
			weather.setCloudsAll(cloudsObj.getInt("all"));
		} catch (JSONException e) {
			// TODO log
		}
		
		
		try{
			JSONArray weatherObj = data.getJSONArray("weather");
			JSONObject weatherFirst = weatherObj.getJSONObject(0);
			weather.setMainDesc(weatherFirst.getString("main"));
			weather.setDecsription(weatherFirst.getString("description"));
			weather.setIcon(weatherFirst.getString("icon"));
		}catch(JSONException e){
			// TODO log
		}
		
		weather.setUpdateTime(Helper.getCurrentTime());
		
		return weather;		
	}
	
}
