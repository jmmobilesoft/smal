package sk.jmmobilesoft.smartalarm.weather.model;

import java.util.Calendar;
import java.util.Date;

public class WeatherForecast {
	
	private int id;
	
	private String cityName;
	private int cloudsAll;
	private String country;
	private String decsription;
	private int humidity;
	private String icon;
	private String latitude;
	private String longitude;
	private String mainDesc;
	private int pressure;
	private long sunrise;
	private long sunset;
	private int tempMax;
	private float temperature;
	private int tempMin;
	private float windDeg;
	private float windSpeed;
	private Calendar updateTime;
	
	public WeatherForecast(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getCloudsAll() {
		return cloudsAll;
	}

	public void setCloudsAll(int cloudsAll) {
		this.cloudsAll = cloudsAll;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDecsription() {
		return decsription;
	}

	public void setDecsription(String decsription) {
		this.decsription = decsription;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMainDesc() {
		return mainDesc;
	}

	public void setMainDesc(String mainDesc) {
		this.mainDesc = mainDesc;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public long getSunrise() {
		return sunrise;
	}

	public void setSunrise(long sunrise) {
		this.sunrise = sunrise;
	}

	public long getSunset() {
		return sunset;
	}

	public void setSunset(long sunset) {
		this.sunset = sunset;
	}

	public int getTempMax() {
		return tempMax;
	}

	public void setTempMax(int tempMax) {
		this.tempMax = tempMax;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public float getWindDeg() {
		return windDeg;
	}

	public void setWindDeg(float windDeg) {
		this.windDeg = windDeg;
	}

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Calendar getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Calendar updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "WeatherForecast [id=" + id + ", cityName=" + cityName
				+ ", cloudsAll=" + cloudsAll + ", country=" + country
				+ ", decsription=" + decsription + ", humidity=" + humidity
				+ ", icon=" + icon + ", latitude=" + latitude + ", longitude="
				+ longitude + ", mainDesc=" + mainDesc + ", pressure="
				+ pressure + ", sunrise=" + sunrise + ", sunset=" + sunset
				+ ", tempMax=" + tempMax + ", temperature=" + temperature
				+ ", tempMin=" + tempMin + ", windDeg=" + windDeg
				+ ", windSpeed=" + windSpeed + ", updateTime=" + updateTime.getTime()
				+ "]";
	}
}
