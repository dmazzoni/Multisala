package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import multisala.exceptions.ReservationException;

public class UserMA extends GuestMA implements IUser {
	
	protected String username;
	
	public UserMA(IAuthServer authServer, ICentralServer centralServer, String username) {
		super(authServer, centralServer);
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
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

	@Override
	public void insertReservation(Reservation res) throws ReservationException {
		try {
			centralServer.insertReservation(res);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	@Override
	public void editReservation(Reservation updated) throws ReservationException {
		try {
			centralServer.editReservation(updated);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	@Override
	public void deleteReservation(int id) {
		try {
			centralServer.deleteReservation(id);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

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
