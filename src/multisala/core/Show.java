package multisala.core;

import java.io.Serializable;

/**
 * Uno spettacolo.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public class Show implements Serializable {
	
	/**
	 * Il codice identificativo dello spettacolo.
	 */
	private int id;
	
	/**
	 * Il titolo del film.
	 */
	private String title;
	
	/**
	 * La data della proiezione.
	 */
	private String date;
	
	/**
	 * L'ora della proiezione.
	 */
	private String time;
	
	/**
	 * Il nome della sala.
	 */
	private String theater;
	
	/**
	 * Il numero di posti liberi.
	 */
	private int freeSeats;
	
	public Show(int id, String title, String date, String time, String theater, int freeSeats) {
		this.id = id;
		this.title = title;
	    this.date = date;
		this.time = time;
		this.theater = theater;
		this.freeSeats = freeSeats;
	}

	/**
	 * @return Il codice dello spettacolo.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return Il titolo del film.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return La data.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return L'ora.
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @return Il nome della sala.
	 */
	public String getTheater() {
		return theater;
	}
	
	/**
	 * @return Il numero di posti liberi.
	 */
	public int getFreeSeats() {
		return freeSeats;
	}
	
}
