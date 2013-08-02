package multisala.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Show implements Serializable {
	
	private int id;
	private String title;
	private Calendar time;
	private String theater;
	
	public Show(int id, String title, int year, int month, int day, int hour, int min, String theater) {
		this.id = id;
		this.title = title;
		this.theater = theater;
		Calendar c = new GregorianCalendar();
		c.set(year, month, day, hour, min);
		this.time = c;
	}

	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public Calendar getTime() {
		return time;
	}

	public String getTheater() {
		return theater;
	}
	
}
