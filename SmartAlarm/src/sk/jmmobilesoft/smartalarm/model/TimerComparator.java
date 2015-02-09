package sk.jmmobilesoft.smartalarm.model;

import java.util.Comparator;

public class TimerComparator implements Comparator<Timer> {

	@Override
	public int compare(Timer lhs, Timer rhs) {
		System.out.println(Integer.valueOf(lhs.getHours() * 10000 + lhs.getMinutes() * 100
				+ lhs.getSeconds()) + " - " + Integer.valueOf(rhs.getHours()
				* 10000 + rhs.getMinutes() * 100 + rhs.getSeconds()));
		return (Integer.valueOf(lhs.getHours() * 10000 + lhs.getMinutes() * 100
				+ lhs.getSeconds()).compareTo(Integer.valueOf(rhs.getHours()
				* 10000 + rhs.getMinutes() * 100 + rhs.getSeconds())));
	}
}
