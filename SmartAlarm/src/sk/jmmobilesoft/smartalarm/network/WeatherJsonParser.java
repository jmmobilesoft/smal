package sk.jmmobilesoft.smartalarm.network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sk.jmmobilesoft.smartalarm.helpers.Helper;
import sk.jmmobilesoft.smartalarm.log.Logger;
import sk.jmmobilesoft.smartalarm.model.Weather;
import sk.jmmobilesoft.smartalarm.model.WeatherForecast;

public class WeatherJsonParser {

	public WeatherForecast parseWeatherForecastData(String dataS) {

		JSONObject data = null;
		try {
			data = new JSONObject(dataS);
		} catch (JSONException e1) {
			Logger.logStackTrace(e1.getStackTrace());
		}

		WeatherForecast weather = new WeatherForecast();

		try {
			JSONObject corObject = data.getJSONObject("coord");
			weather.setLatitude(corObject.getString("lat"));
			weather.setLongitude(corObject.getString("lon"));
		} catch (JSONException e) {
			Logger.logStackTrace(e.getStackTrace());
		}

		try {
			JSONObject sysObj = data.getJSONObject("sys");
			weather.setCountry(sysObj.getString("country"));
			weather.setSunrise(sysObj.getLong("sunrise"));
			weather.setSunset(sysObj.getLong("sunset"));
		} catch (JSONException e) {
			Logger.logStackTrace(e.getStackTrace());
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
			Logger.logStackTrace(e.getStackTrace());
		}

		try {
			JSONArray weatherObj = data.getJSONArray("weather");
			JSONObject weatherFirst = weatherObj.getJSONObject(0);
			weather.setMainDesc(weatherFirst.getString("main"));
			weather.setDescription(weatherFirst.getString("description"));
			weather.setIcon(weatherFirst.getString("icon"));
		} catch (JSONException e) {
			Logger.logStackTrace(e.getStackTrace());
		}

		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
		String updateTime = f.format(Helper.getCurrentTime().getTime());
		weather.setUpdateTime(updateTime);

		return weather;
	}

	public List<Weather> parseWeatherData(String dataS) {

		JSONObject data = null;
		try {
			data = new JSONObject(dataS);
		} catch (JSONException e1) {
			Logger.logStackTrace(e1.getStackTrace());
		}

		List<Weather> weathers = new ArrayList<>();
		try {
			for (int i = 1; i <= 3; i++) {
				Weather weather = new Weather();
				JSONObject cityObj = data.getJSONObject("city"); 
				weather.setCity(cityObj.getString("name"));

				JSONArray dataList = data.getJSONArray("list");
				JSONObject dayObject = dataList
						.getJSONObject(i);
				JSONArray wA = dayObject.optJSONArray("weather");
				JSONObject w = wA.getJSONObject(0);
				weather.setDescription(w.getString("description"));
				weather.setIcon(w.getString("icon"));
				JSONObject tempObject = dayObject.getJSONObject("temp");
				weather.setTempMin((float) tempObject.getDouble("night"));
				weather.setTempMax((float) tempObject.getDouble("day"));
				SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
				weather.setDate(formater.format(
						Helper.milisToDate(dayObject.getInt("dt"))).toString());
				weathers.add(weather);
			}
		} catch (JSONException e) {
			Logger.logStackTrace(e.getStackTrace());
		}

		return weathers;
	}

}
