package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import multisala.exceptions.ReservationException;

/**
 * L'agente mobile per l'utente standard.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 * 
 */
public class UserMA extends GuestMA implements IUser {
	
	/**
	 * Il nome dell'utente loggato.
	 */
	protected String username;
	
	public UserMA(IAuthServer authServer, ICentralServer centralServer, String username) {
		super(authServer, centralServer);
		this.username = username;
	}

	/**
	 * Fornisce il nome dell'utente loggato.
	 * @return Il nome utente.
	 */
	@Override
	public String getUsername() {
		return username;
	}
	
	/**
	 * Fornisce l'elenco di prenotazioni effettuate dall'utente corrente.
	 * @return La lista delle prenotazioni.
	 * @see Reservation
	 */
	@Override
	public List<Reservation> getReservations() {
		List<Reservation> reservations = new Vector<Reservation>();
		try {
			reservations = centralServer.getReservations(username);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
		return reservations;
	}

	/**
	 * Inserisce una nuova prenotazione per l'utente.
	 * @param res la prenotazione da inserire 
	 */
	@Override
	public void insertReservation(Reservation res) throws ReservationException {
		try {
			centralServer.insertReservation(res);
			window.setStatus("Prenotazione effettuata con successo");
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * Modifica il numero di posti di una prenotazione dell'utente.
	 * @param updated la prenotazione col numero di posti aggiornato
	 */
	@Override
	public void editReservation(Reservation updated) throws ReservationException {
		try {
			centralServer.editReservation(updated);
			window.setStatus("Prenotazione aggiornata con successo");
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * Elimina una prenotazione specificata.
	 * @param id l'id della prenotazione da eliminare
	 */
	@Override
	public void deleteReservation(int id) {
		try {
			centralServer.deleteReservation(id);
			window.setStatus("Prenotazione annullata");
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * Ottiene dal server di autenticazione un nuovo agente mobile di tipo "ospite" 
	 * e lo ritorna, permettendo al chiamante di sostituire il corrente agente del client
	 * registrato con quest'ultimo.
	 * @return L'agente mobile per il client non loggato.
	 * 
	 */
	@Override
	public IGuest logout() {
		IGuest guestAgent = null;
		try {
			guestAgent = authServer.login();
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(window, e);
		}
		return guestAgent;
	}

}
