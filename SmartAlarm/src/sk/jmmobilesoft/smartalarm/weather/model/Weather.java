package sk.jmmobilesoft.smartalarm.weather.model;

public class Weather {

	private int id;
	
	private String mainDesc;
	
	private String decsription;
	
	private String icon;

	public Weather(){}
	
	public Weather(int id, String mainDesc, String decsription, String icon) {
		this.id = id;
		this.mainDesc = mainDesc;
		this.decsription = decsription;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMainDesc() {
		return mainDesc;
	}

	public void setMainDesc(String mainDesc) {
		this.mainDesc = mainDesc;
	}

	public String getDecsription() {
		return decsription;
	}

	public void setDecsription(String decsription) {
		this.decsription = decsription;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
	
	
	
}
