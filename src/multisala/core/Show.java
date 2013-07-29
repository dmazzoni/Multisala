package multisala.core;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Show {
	
	private String title;
	private Calendar time;
	private String theater;
	
	public Show(String title, int year, int month, int day, int hour, int min, String theater) {
		this.title = title;
		this.theater = theater;
		Calendar c = new GregorianCalendar();
		c.set(year, month, day, hour, min);
		this.time = c;
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
