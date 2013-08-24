package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import multisala.gui.AdminUI;
import multisala.gui.ConfirmUsersPanel;
import multisala.gui.ShowManagementPanel;

public class AdminMS extends UserMA implements IAdmin, IAdminMS {
	
	public AdminMS(IAuthServer authServer, ICentralServer centralServer, String username) {
		super(authServer, centralServer, username);
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
			centralServer.sellTickets(sh, tickets);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	@Override
	public List<Reservation> getAllReservations() {
		List<Reservation> reservations = new Vector<Reservation>();
		try {
			reservations = centralServer.getReservations();
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
		return reservations;
	}

	@Override
	public IGuest logout() {
		try {
			centralServer.adminDisconnected(this);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(window, e);
		}
		return super.logout();
	}
	
	@Override
	public void adminConnected() {
		try {
			centralServer.adminConnected(this);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}
	
	@Override
	public List<String> confirmUsers(List<String> users) throws RemoteException {
		ConfirmUsersPanel confirmationPanel = new ConfirmUsersPanel(users);
		JOptionPane.showConfirmDialog(window, confirmationPanel, "Conferma utenti", JOptionPane.OK_OPTION);
		return confirmationPanel.getConfirmedUsers();
	}
	
	@Override
	public void showSoldOut(Show sh) throws RemoteException {
		String message = "Posti esauriti per lo spettacolo " + sh.getTitle() + " del " + sh.getDate() + " " + sh.getTime() + 
				". Inserire un nuovo spettacolo?";
		int result = JOptionPane.showConfirmDialog(window, message, "Posti esauriti", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			ShowManagementPanel sPanel = new ShowManagementPanel((AdminUI) window, sh.getTitle());
			window.getTabbedView().addTab("Nuovo spettacolo", sPanel);
			window.getTabbedView().setSelectedComponent(sPanel);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof AdminMS && ((AdminMS) obj).username.equals(this.username));
	}
	
	@Override
	public int hashCode() {
		return username.hashCode();
	}

}
