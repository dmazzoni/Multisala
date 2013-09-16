package multisala.exceptions;

/**
 * Eccezione lanciata nel caso una prenotazione o un'emissione
 * di biglietti non sia effettuabile.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public class ReservationException extends RuntimeException {
	
	/**
	 * Costruisce una <code>ReservationException</code> con
	 * il messaggio specificato
	 * @param message il messaggio
	 */
	public ReservationException(String message) {
		super(message);
	}

}
