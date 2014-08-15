package sk.jmmobilesoft.smartalarm.model;

import android.net.Uri;

public class Timer {

	private Long id;
	
	private int hours;
	
	private int minutes;
	
	private String name;
	
	private boolean active;
	
	private Uri sound;
	
	private float volume;

	public Timer() {
		id = (long) -1;
	}
	
	public Timer(int hours, int minutes, String name) {
		this.id = (long) -1;
		this.hours = hours;
		this.minutes = minutes;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Uri getSound() {
		return sound;
	}

	public void setSound(Uri sound) {
		this.sound = sound;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "Timer [id=" + id + ", hours=" + hours + ", minutes=" + minutes
				+ ", name=" + name + ", active=" + active + "]";
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
