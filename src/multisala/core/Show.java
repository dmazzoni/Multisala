package multisala.core;

import java.io.Serializable;
import java.text.ParseException;

public class Show implements Serializable {
	
	private int id;
	private String title;
	private String date;
	private String time;
	private int theater;
	private int freeSeats;
	
	public Show(int id, String title, String date, String time, int theater, int freeSeats) {
		this.id = id;
		this.title = title;
		this.theater = theater;
	    this.date = date;
		this.time = time;
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

	public int getTheater() {
		return theater;
	}
	
	public int getFreeSeats() {
		return freeSeats;
	}
	
}
