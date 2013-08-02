package multisala.core;

import java.io.Serializable;

public class Reservation implements Serializable {
	
	private int id;
	private Show show;
	private String user;
	private int seats;
	
	public Reservation(int id, Show show, String user, int seats) {
		this.id = id;
		this.show = show;
		this.user = user;
		this.seats = seats;
	}

	public int getId() {
		return id;
	}
	
	public Show getShow() {
		return show;
	}

	public String getUser() {
		return user;
	}

	public int getSeats() {
		return seats;
	}
}
