package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.AccountException;

import multisala.exceptions.ReservationException;

/**
 * Interfaccia remota del server centrale.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface ICentralServer extends Remote {

	/**
	 * Ritorna la programmazione della data specificata.
	 * @param dt la data
	 * @return La lista degli spettacoli.
	 */
	List<Show> getSchedule(String dt) throws RemoteException, SQLException;
	
	/**
	 * Crea un nuovo account per un utente standard, che deve essere
	 * approvato da un amministratore.
	 * @param user il nome utente
	 * @param password la password
	 * @throws AccountException in caso di nome utente precedentemente assegnato
	 */
	void register(String user, String password) throws AccountException, RemoteException, SQLException;
	
	/**
	 * Ritorna la lista delle prenotazioni effettuate da tutti gli utenti.
	 * @return La lista di prenotazioni.
	 */
	List<Reservation> getReservations() throws RemoteException, SQLException;
	
	/**
	 * Ritorna la lista delle prenotazioni effettuate dall'utente specificato.
	 * @param user il nome utente
	 * @return La lista di prenotazioni.
	 */
	List<Reservation> getReservations(String user) throws RemoteException, SQLException;
	
	/**
	 * Inserisce una nuova prenotazione con i dati specificati.
	 * @param res la prenotazione da inserire
	 * @throws ReservationException in caso di prenotazione non ammissibile
	 */
	void insertReservation(Reservation res) throws RemoteException, ReservationException, SQLException;
	
	/**
	 * Modifica il numero di posti della prenotazione specificata.
	 * @param current la prenotazione attuale
	 * @param updated la prenotazione aggiornata
	 * @throws ReservationException in caso di modifica non ammissibile
	 */
	void editReservation(Reservation current, Reservation updated) throws RemoteException, ReservationException, SQLException;
	
	/**
	 * Elimina la prenotazione specificata.
	 * @param id l'identificativo della prenotazione
	 */
	void deleteReservation(int id) throws RemoteException, SQLException;
	
	/**
	 * Inserisce un nuovo spettacolo con i dati specificati.
	 * @param sh lo spettacolo da inserire
	 */
	void insertShow(Show sh) throws RemoteException, SQLException;
	
	/**
	 * Modifica i dati dello spettacolo specificato.
	 * @param updated lo spettacolo aggiornato
	 */
	void editShow(Show updated) throws RemoteException, SQLException;
	
	/**
	 * Elimina lo spettacolo specificato.
	 * @param id l'identificativo dello spettacolo
	 */
	void deleteShow(int id) throws RemoteException, SQLException;
	
	/**
	 * Emette il numero di biglietti specificato per un dato spettacolo.<br>
	 * Utilizzato in caso di vendita diretta senza prenotazione.
	 * @param sh lo spettacolo
	 * @param tickets il numero di biglietti
	 * @throws ReservationException se non ci sono abbastanza posti liberi
	 */
	void sellTickets(Show sh, int tickets) throws RemoteException, ReservationException, SQLException;
	
	/**
	 * Registra nell'insieme degli amministratori in linea 
	 * il mobile server specificato.
	 * @param admin il mobile server dell'amministratore
	 */
	void adminConnected(IAdminMS admin) throws RemoteException;
	
	/**
	 * Rimuove dall'insieme degli amministratori in linea
	 * il mobile server specificato.
	 * @param admin il mobile server dell'amministratore
	 */
	void adminDisconnected(IAdminMS admin) throws RemoteException;
	
}
