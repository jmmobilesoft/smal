package sk.jmmobilesoft.smartalarm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.net.Uri;


public class Clock {
	
	private long id;
	private int hour;
	private int minutes;
	private boolean active;
	private Uri sound;
	private String name;
	private int snoozeTime;
	private float volume;  
	private List<String> cities;
	//private boolean niceWakeUp; //TODO
	private boolean vibrate = false;
	private int[] repeat = new int[]{0,0,0,0,0,0,0};
	
	public Clock(){
		id = -1l;
		cities = new ArrayList<String>();
	}
	
	public Clock(int hour, int minutes, boolean active, String name,int[] repeat){
		id = -1l;
		this.hour = hour;
		this.minutes = minutes;
		this.name = name;
		this.active = active;
		this.repeat = repeat;
		cities = new ArrayList<String>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
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

	public Uri getSound() {
		return sound;
	}

	public void setSound(Uri sound) {
		this.sound = sound;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getRepeat() {
		return repeat;
	}

	public void setRepeat(int[] repeat) {
		this.repeat = repeat;
	}
	
	public int getSnoozeTime() {
		return snoozeTime;
	}

	public void setSnoozeTime(int snoozeTime) {
		this.snoozeTime = snoozeTime;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

	public boolean isVibrate() {
		return vibrate;
	}

	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

	public String toDBRepeat(int[] repeats){
		String result = String.valueOf(repeats[0]) + String.valueOf(repeats[1]) + String.valueOf(repeats[2] + String.valueOf(repeats[3]) +
				String.valueOf(repeats[4]) + String.valueOf(repeats[5]) + String.valueOf(repeats[6]));
		return result;
	}
	public int[] fromDBRepeat(String value){
		int[] result = new int[7];
		for(int i = 0; i < 7; i++){
			result[i] = Integer.parseInt(Character.toString(value.charAt(i)), 2);
		}
		return result;
	}

	@Override
	public String toString() {
		return "Clock [id=" + id + ", hour=" + hour + ", minutes=" + minutes
				+ ", active=" + active + ", name=" + name + ", vibrate=" + vibrate + ", repeat="
				+ Arrays.toString(repeat) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + hour;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + minutes;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(repeat);
		result = prime * result + ((sound == null) ? 0 : sound.hashCode());
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
		Clock other = (Clock) obj;
		if (active != other.active)
			return false;
		if (hour != other.hour)
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
		if (!Arrays.equals(repeat, other.repeat))
			return false;
		if (sound == null) {
			if (other.sound != null)
				return false;
		} else if (!sound.equals(other.sound))
			return false;
		return true;
	}		
}
