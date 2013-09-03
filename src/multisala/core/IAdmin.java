package multisala.core;

import java.util.List;

/**
 * Interfaccia dell'agente mobile dell'amministratore.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface IAdmin extends IUser {

	/**
	 * Inserisce un nuovo spettacolo.
	 * @param sh lo spettacolo da inserire
	 */
	void insertShow(Show sh);
	
	/**
	 * Modifica uno spettacolo esistente.
	 * @param updated lo spettacolo con i dati modificati
	 */
	void editShow(Show updated);
	
	/**
	 * Elimina uno spettacolo specificato.
	 * @param id l'id dello spettacolo da eliminare
	 */
	void deleteShow(int id);
	
	/**
	 * Permette di effettuare la vendita diretta alla cassa,
	 * per i clienti che acquistano i biglietti per uno spettacolo
	 * senza aver effettuato la prenotazione.
	 * @param sh lo spettacolo di cui si vogliono vendere i biglietti
	 * @param tickets il numero di biglietti richiesti
	 */
	void sellTickets(Show sh, int tickets);
	
	/**
	 * Mostra all'amministratore tutte le prenotazioni presenti nel sistema.
	 * @return La lista di prenotazioni.
	 * @see Reservation
	 */
	List<Reservation> getAllReservations();
	
	/**
	 * Segnala al server centrale che un utente amministratore si è connesso.
	 */
	void adminConnected();
	
}
