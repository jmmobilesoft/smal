package sk.jmmobilesoft.smartalarm.model;

import android.net.Uri;

public class Timer {

	private long id;

	private int hours;

	private int minutes;

	private int seconds;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
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
				+ ", seconds=" + seconds + ", name=" + name + ", active="
				+ active + ", sound=" + sound + ", volume=" + volume + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + hours;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + minutes;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + seconds;
		result = prime * result + ((sound == null) ? 0 : sound.hashCode());
		result = prime * result + Float.floatToIntBits(volume);
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
		if (active != other.active)
			return false;
		if (hours != other.hours)
			return false;
		if (id != other.id)
			return false;
		if (minutes != other.minutes)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (seconds != other.seconds)
			return false;
		if (sound == null) {
			if (other.sound != null)
				return false;
		} else if (!sound.equals(other.sound))
			return false;
		if (Float.floatToIntBits(volume) != Float.floatToIntBits(other.volume))
			return false;
		return true;
	}

}
