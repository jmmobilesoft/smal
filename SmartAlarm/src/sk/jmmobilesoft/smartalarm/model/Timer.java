package sk.jmmobilesoft.smartalarm.model;

public class Timer {

	private Long id;
	
	private int hours;
	
	private int minutes;
	
	private boolean active;

	public Timer(Long id, int hours, int minutes) {
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Timer [id=" + id + ", hours=" + hours + ", minutes=" + minutes
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + minutes;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timer other = (Timer) obj;
		if (hours != other.hours)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}
}
