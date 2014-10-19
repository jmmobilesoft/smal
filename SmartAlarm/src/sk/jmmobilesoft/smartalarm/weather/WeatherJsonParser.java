package sk.jmmobilesoft.smartalarm.weather;

import org.json.JSONException;
import org.json.JSONObject;

import sk.jmmobilesoft.smartalarm.weather.model.Coordinates;
import sk.jmmobilesoft.smartalarm.weather.model.MainInfo;
import sk.jmmobilesoft.smartalarm.weather.model.SysWeather;
import sk.jmmobilesoft.smartalarm.weather.model.Weather;
import sk.jmmobilesoft.smartalarm.weather.model.WeatherForecast;

public class WeatherJsonParser {

	public WeatherForecast parseData(String dataS){
		
		JSONObject data = null;
		try {
			data = new JSONObject(dataS);
		} catch (JSONException e1) {
			// TODO log cant parse
		}
		
		Coordinates loc = new Coordinates();
		
		try {
			JSONObject corObject = data.getJSONObject("coord");
			loc.setLatitude(corObject.getString("lat"));
			loc.setLongitude(corObject.getString("lon"));
		} catch (JSONException e) {
			//TODO log
		}
		
		SysWeather sys = new SysWeather();
		
		try {
			JSONObject sysObj = data.getJSONObject("sys");
			sys.setCountry(sysObj.getString("country"));
			sys.setSunrise(data.getLong("sunrise"));
			sys.setSunset(data.getLong("sunset"));			
		} catch (JSONException e) {
			// TODO log
		}

		MainInfo info = new MainInfo();
		
		try {
			JSONObject infoObj = data.getJSONObject("main");
			info.setCityName(data.getString("name"));
			info.setTemperature((float) infoObj.getDouble("temp"));
			info.setHumidity(infoObj.getInt("humidity"));
			info.setPressure(infoObj.getInt("pressure"));
			info.setTempMin(infoObj.getInt("temp_min"));
			info.setTempmax(infoObj.getInt("temp_max"));
			
			JSONObject windObj = data.getJSONObject("wind");
			info.setWindSpeed((float) windObj.getDouble("speed"));
			info.setWindDeg((float) windObj.getDouble("deg"));
		} catch (JSONException e) {
			// TODO log
		}
		
		Weather weather = new Weather();
		
		try{
			JSONObject weatherObj = data.getJSONObject("weather");
			weather.setMainDesc(weatherObj.getString("main"));
			weather.setDecsription(weatherObj.getString("description"));
			weather.setIcon(weatherObj.getString("icon"));
		}catch(JSONException e){
			// TODO log
		}
		
		WeatherForecast weatherFor = new WeatherForecast(loc, info, sys, weather);
		
		return weatherFor;		
	}
	
}
