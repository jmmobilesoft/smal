package sk.jmmobilesoft.smartalarm.weather.model;

public class WeatherForecast {

	private Coordinates coordinates;
	
	private MainInfo mainInfo;
	
	private SysWeather sys;
	
	private Weather weather;

	public WeatherForecast(){}
	
	public WeatherForecast(Coordinates coordinates, MainInfo mainInfo,
			SysWeather sys, Weather weather) {
		super();
		this.coordinates = coordinates;
		this.mainInfo = mainInfo;
		this.sys = sys;
		this.weather = weather;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public MainInfo getMainInfo() {
		return mainInfo;
	}

	public void setMainInfo(MainInfo mainInfo) {
		this.mainInfo = mainInfo;
	}

	public SysWeather getSys() {
		return sys;
	}

	public void setSys(SysWeather sys) {
		this.sys = sys;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}
}
