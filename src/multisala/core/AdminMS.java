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
	 * Inserisce un nuovo spettacolo.
	 * @param sh lo spettacolo da inserire
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
	 * Modifica uno spettacolo esistente.
	 * @param updated lo spettacolo con i dati modificati
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
	 * Elimina uno spettacolo specificato.
	 * @param id l'id dello spettacolo da eliminare
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
	 * Permette di effettuare la vendita diretta alla cassa,
	 * per i clienti che acquistano i biglietti per uno spettacolo
	 * senza aver effettuato la prenotazione.
	 * @param sh lo spettacolo di cui si vogliono vendere i biglietti
	 * @param tickets il numero di biglietti richiesti
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
	 * Mostra all'amministratore tutte le prenotazioni presenti nel sistema.
	 * @return La lista di prenotazioni.
	 * @see Reservation
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
	 * Segnala al server centrale che un utente amministratore si è connesso.
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
	 * Invocato dal server centrale per notificare all'amministratore la presenza 
	 * di nuovi utenti che hanno richiesto la registrazione nel sistema. 
	 * Ne mostra l'elenco in una finestra di dialogo e permette di 
	 * selezionare quali approvare.
	 * @param users la lista degli utenti in attesa di conferma
	 * @return La lista degli utenti approvati.
	 */
	@Override
	public List<String> confirmUsers(List<String> users) throws RemoteException {
		ConfirmUsersPanel confirmationPanel = new ConfirmUsersPanel(users);
		JOptionPane.showConfirmDialog(window, confirmationPanel, "Conferma utenti", JOptionPane.OK_OPTION);
		return confirmationPanel.getConfirmedUsers();
	}
	
	/**
	 * Invocato dal server centrale per informare l'amministratore che
	 * sono stati venduti tutti i biglietti per un dato spettacolo.
	 * @param sh lo spettacolo di cui sono esauriti i posti disponibili
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
	 * Necessario per la gestione dell'insieme di utenti connessi
	 * lato server centrale.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof AdminMS && ((AdminMS) obj).username.equals(this.username));
	}
	
	@Override
	public int hashCode() {
		return username.hashCode();
	}

}
