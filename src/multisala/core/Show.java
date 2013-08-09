package multisala.core;

import java.io.Serializable;
import java.text.ParseException;

public class Show implements Serializable {
	
	private int id;
	private String title;
	private String date;
	private String time;
	private String theater;
	
	public Show(int id, String title, String date, String time, String theater) 
			throws ParseException {
		this.id = id;
		this.title = title;
		this.theater = theater;
	    this.date = date;
		this.time = time;
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
	
}
