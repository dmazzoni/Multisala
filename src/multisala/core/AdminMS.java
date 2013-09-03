package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import multisala.gui.AbstractListPanel;
import multisala.gui.AdminUI;
import multisala.gui.ConfirmUsersPanel;
import multisala.gui.ShowManagementPanel;

/**
 * Il mobile server per l'utente amministratore.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 * 
 */
public class AdminMS extends UserMA implements IAdmin, IAdminMS {
	
	public AdminMS(IAuthServer authServer, ICentralServer centralServer, String username) {
		super(authServer, centralServer, username);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertShow(Show sh) {
		try {
			centralServer.insertShow(sh);
			window.setStatus("Spettacolo inserito con successo");
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void editShow(Show updated) {
		try {
			centralServer.editShow(updated);
			window.setStatus("Spettacolo aggiornato con successo");
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteShow(int id) {
		try {
			centralServer.deleteShow(id);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sellTickets(Show sh, int tickets) {
		try {
			centralServer.sellTickets(sh, tickets);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * {@inheritDoc} <br>
	 * Notifica inoltre al server centrale la disconnessione del client
	 * amministratore.
	 */
	@Override
	public IGuest logout() {
		try {
			centralServer.adminDisconnected(this);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(window, e);
		}
		return super.logout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void adminConnected() {
		try {
			centralServer.adminConnected(this);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> confirmUsers(List<String> users) throws RemoteException {
		ConfirmUsersPanel confirmationPanel = new ConfirmUsersPanel(users);
		JOptionPane.showConfirmDialog(window, confirmationPanel, "Conferma utenti", JOptionPane.OK_OPTION);
		return confirmationPanel.getConfirmedUsers();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showSoldOut(Show sh) throws RemoteException {
		String message = "Posti esauriti per lo spettacolo " + sh.getTitle() + " del " + sh.getDate() + " " + sh.getTime() + 
				". Inserire un nuovo spettacolo?";
		int result = JOptionPane.showConfirmDialog(window, message, "Posti esauriti", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			AbstractListPanel scheduleTab = (AbstractListPanel) window.getTabbedView().getComponentAt(0);
			ShowManagementPanel sPanel = new ShowManagementPanel((AdminUI) window, scheduleTab, sh.getTitle());
			window.getTabbedView().addTab("Nuovo spettacolo", sPanel);
			window.getTabbedView().setSelectedComponent(sPanel);
		}
	}
	
	/**
	 * Necessario per la gestione dell'insieme di amministratori 
	 * connessi al server centrale.
	 * @param obj l'oggetto da confrontare
	 * @return <code>true</code> se <b>obj</b> equivale a <b>this</b>, <code>false</code> altrimenti
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof AdminMS && ((AdminMS) obj).username.equals(this.username));
	}
	
	/**
	 * Restituisce l'hashcode del mobile server, basandosi sul
	 * nome utente dell'amministratore.
	 * @return l'hashcode del mobile server
	 */
	@Override
	public int hashCode() {
		return username.hashCode();
	}

}
