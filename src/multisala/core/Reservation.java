package multisala.core;

import java.io.Serializable;

/**
 * Una prenotazione per uno spettacolo, effettuata da
 * un utente registrato.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public class Reservation implements Serializable {
	
	/**
	 * Il codice della prenotazione.
	 */
	private int id;
	
	/**
	 * Lo spettacolo prenotato.
	 */
	private Show show;
	
	/**
	 * Il nome dell'utente che ha effettuato la prenotazione.
	 */
	private String user;
	
	/**
	 * Il numero di posti prenotati.
	 */
	private int seats;
	
	public Reservation(int id, Show show, String user, int seats) {
		this.id = id;
		this.show = show;
		this.user = user;
		this.seats = seats;
	}

	/**
	 * @return Il codice della prenotazione.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return Lo spettacolo prenotato.
	 * @see Show
	 */
	public Show getShow() {
		return show;
	}

	/**
	 * @return Il nome utente.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return Il numero di posti.
	 */
	public int getSeats() {
		return seats;
	}
	
}
