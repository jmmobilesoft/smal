package sk.jmmobilesoft.smartalarm.model;

import java.util.Date;

public class Timer {

	private Long id;
	
	private Date timer;

	public Timer(Long id, Date timer) {
		super();
		this.id = id;
		this.timer = timer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimer() {
		return timer;
	}
	
	public String getStringTimer(){
		String result = timer.getHours() + ":" + timer.getMinutes();
		return result;
	}

	public void setTimer(Date timer) {
		this.timer = timer;
	}

	@Override
	public String toString() {
		return "Timer [id=" + id + ", timer=" + timer + "]";
	}
	
	
	
}
