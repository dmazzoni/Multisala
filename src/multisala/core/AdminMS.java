package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import multisala.gui.ConfirmUsersPanel;

public class AdminMS extends UserMA implements IAdmin, IAdminMS {
	
	public AdminMS(IAuthServer authServer, ICentralServer centralServer) {
		super(authServer, centralServer);
	}

	@Override
	public void insertShow(Show sh) {
		try {
			centralServer.insertShow(sh);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	@Override
	public void editShow(Show updated) {
		try {
			centralServer.editShow(updated);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	@Override
	public void deleteShow(int id) {
		try {
			centralServer.deleteShow(id);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	@Override
	public void sellTickets(Show sh, int tickets) {
		try {
			centralServer.sellTickets(sh.getId(), tickets);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	@Override
	public List<Reservation> getReservations() {
		List<Reservation> reservations = new Vector<Reservation>();
		try {
			reservations = centralServer.getReservations();
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
		return reservations;
	}

	@Override
	public List<String> confirmUsers(List<String> users) {
		ConfirmUsersPanel confirmationPanel = new ConfirmUsersPanel(users);
		JOptionPane.showConfirmDialog(window, confirmationPanel, "Conferma utenti", JOptionPane.OK_OPTION);
		return confirmationPanel.getConfirmedUsers();
	}

}
