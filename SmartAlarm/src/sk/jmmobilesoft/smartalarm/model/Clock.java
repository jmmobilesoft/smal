package sk.jmmobilesoft.smartalarm.model;


public class Clock {

	private long id;
	private boolean active;
	private String time;
	
	public Clock(String time,boolean active){
		this.time = time;
		this.active = active;
	}

	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Clock [active=" + active + ", time=" + time + "]";
	}
	
		
}
