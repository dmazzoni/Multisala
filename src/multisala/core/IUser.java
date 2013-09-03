package multisala.core;

import java.util.List;

import multisala.exceptions.ReservationException;

/**
 * Interfaccia dell'agente mobile per l'utente standard.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface IUser extends IGuest {

	/**
	 * Fornisce il nome dell'utente loggato.
	 * @return Il nome utente.
	 */
	String getUsername();
	
	/**
	 * Fornisce l'elenco di prenotazioni effettuate dall'utente corrente.
	 * @return La lista delle prenotazioni.
	 * @see Reservation
	 */
	List<Reservation> getReservations();
	
	/**
	 * Inserisce una nuova prenotazione per l'utente.
	 * @param res la prenotazione da inserire
	 * @throws ReservationException se i posti liberi sono insufficienti
	 */
	void insertReservation(Reservation res) throws ReservationException;
	
	/**
	 * Modifica il numero di posti di una prenotazione dell'utente.
	 * @param updated la prenotazione col numero di posti aggiornato
	 * @throws ReservationException se i posti liberi sono insufficienti
	 */
	void editReservation(Reservation current, Reservation updated) throws ReservationException;
	
	/**
	 * Elimina la prenotazione specificata.
	 * @param id l'id della prenotazione da eliminare
	 */
	void deleteReservation(int id);
	
	/**
	 * Ottiene dal server di autenticazione un nuovo agente mobile di tipo {@link IGuest}
	 * e lo ritorna, permettendo al chiamante di sostituire il corrente agente del client
	 * registrato con quest'ultimo.
	 * @return L'agente mobile per il client non loggato.
	 */
	IGuest logout();
	
}
