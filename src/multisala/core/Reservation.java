package multisala.core;

public class Reservation {
	
	private Show show;
	private String user;
	private int seats;
	
	public Reservation(Show show, String user, int seats) {
		this.show = show;
		this.user = user;
		this.seats = seats;
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
