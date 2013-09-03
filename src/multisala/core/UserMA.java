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
	 * {@inheritDoc}
	 */
	@Override
	public String getUsername() {
		return username;
	}
	
	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	@Override
	public void editReservation(Reservation current, Reservation updated) throws ReservationException {
		try {
			centralServer.editReservation(current, updated);
			window.setStatus("Prenotazione aggiornata con successo");
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
