package sk.jmmobilesoft.smartalarm.weather.model;

public class MainInfo {

	private String cityName;
	
	private float temperature;
	
	private int humidity;
	
	private int pressure;
	
	private int tempMin;
	
	private int tempmax;
	
	private float windSpeed;
	
	private float windDeg;
	
	private int cloudsAll;
	
	public MainInfo(){}

	public MainInfo(String cityName, float temperature, int humidity,
			int pressure, int tempMin, int tempmax, float windSpeed,
			float windDeg, int cloudsAll) {
		this.cityName = cityName;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.tempMin = tempMin;
		this.tempmax = tempmax;
		this.windSpeed = windSpeed;
		this.windDeg = windDeg;
		this.cloudsAll = cloudsAll;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public int getTempmax() {
		return tempmax;
	}

	public void setTempmax(int tempmax) {
		this.tempmax = tempmax;
	}

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public float getWindDeg() {
		return windDeg;
	}

	public void setWindDeg(float windDeg) {
		this.windDeg = windDeg;
	}

	public int getCloudsAll() {
		return cloudsAll;
	}

	public void setCloudsAll(int cloudsAll) {
		this.cloudsAll = cloudsAll;
	}	
}
