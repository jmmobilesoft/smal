package sk.jmmobilesoft.smartalarm.weather.model;

public class SysWeather {

	private String country;
	private long sunrise;
	private long sunset;
	
	public SysWeather(){}
	
	public SysWeather(String country, long sunrise, long sunset) {
		this.country = country;
		this.sunrise = sunrise;
		this.sunset = sunset;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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
}
