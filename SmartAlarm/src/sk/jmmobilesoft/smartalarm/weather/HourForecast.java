package sk.jmmobilesoft.smartalarm.weather;

public class HourForecast {

	private int actualTemperature;
	private int maxTemperature;
	private int minTemperature;
	private int humidity;
	private String wind;
	private String weatherTitle;
	
	public HourForecast(){
		
	}
	
	public int getActualTemperature() {
		return actualTemperature;
	}
	public void setActualTemperature(int actualTemperature) {
		this.actualTemperature = actualTemperature;
	}
	public int getMaxTemperature() {
		return maxTemperature;
	}
	public void setMaxTemperature(int maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
	public int getMinTemperature() {
		return minTemperature;
	}
	public void setMinTemperature(int minTemperature) {
		this.minTemperature = minTemperature;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}
	public String getWeatherTitle() {
		return weatherTitle;
	}
	public void setWeatherTitle(String weatherTitle) {
		this.weatherTitle = weatherTitle;
	}
}
