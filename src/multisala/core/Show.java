package multisala.core;

import java.io.Serializable;

public class Show implements Serializable {
	
	private int id;
	private String title;
	private String date;
	private String time;
	private String theater;
	private int freeSeats;
	
	public Show(int id, String title, String date, String time, String theater, int freeSeats) {
		this.id = id;
		this.title = title;
	    this.date = date;
		this.time = time;
		this.theater = theater;
		this.freeSeats = freeSeats;
	}

	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getTheater() {
		return theater;
	}
	
	public int getFreeSeats() {
		return freeSeats;
	}
	
}
