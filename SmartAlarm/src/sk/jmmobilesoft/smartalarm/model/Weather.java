package sk.jmmobilesoft.smartalarm.model;

import java.util.Date;

public class Weather {

	private long id;

	private String city;
	private String date;
	private float tempMin;
	private float tempMax;
	private String description;
	private String icon;

	public Weather() {
		id = 1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getTempMin() {
		return tempMin;
	}

	public void setTempMin(float tempMin) {
		this.tempMin = tempMin;
	}

	public float getTempMax() {
		return tempMax;
	}

	public void setTempMax(float tempMax) {
		this.tempMax = tempMax;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Weather [id=" + id + ", city=" + city + ", date=" + date
				+ ", tempMin=" + tempMin + ", tempMax=" + tempMax
				+ ", description=" + description + ", icon=" + icon + "]";
	}
	

}
