package sk.jmmobilesoft.smartalarm.model;

public class Timer {

	private Long id;
	
	private int hours;
	
	private int minutes;

	public Timer(Long id, int hours, int minutes) {
		super();
		this.id = id;
		this.hours = hours;
		this.minutes = minutes;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	@Override
	public String toString() {
		return "Timer [id=" + id + ", hours=" + hours + ", minutes=" + minutes
				+ "]";
	}
}
