package sk.jmmobilesoft.smartalarm.model;

import java.util.Comparator;

public class ClockComparator implements Comparator<Clock> {

	@Override
	public int compare(Clock lhs, Clock rhs) {
		return (Integer.valueOf(lhs.getHour() * 100 + lhs.getMinutes()))
				.compareTo(Integer.valueOf(rhs.getHour() * 100 + rhs.getMinutes()));
	}

}
