package multisala.gui;

import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;

/**
 * Il pannello che mostra l'elenco di utenti che richiedono la registrazione
 * al sistema, e ne permette l'approvazione.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 *
 */
public class ConfirmUsersPanel extends JPanel {
	
	/**
	 * La lista degli utenti in attesa di conferma
	 */
	List<String> users;
	
	/**
	 * Il componente grafico della lista degli utenti in attesa di conferma
	 */
	JList<String> confirmationList;

	public ConfirmUsersPanel(List<String> users) {
		this.users = users;
		this.confirmationList = new JList<String>(users.toArray(new String[users.size()]));
	}

	/**
	 * Restituisce l'elenco degli utenti che sono stati selezionati dall'amministratore
	 * dall'elenco grafico per l'approvazione.
	 * @return La lista degli utenti approvati.
	 */
	public List<String> getConfirmedUsers() {
		return confirmationList.getSelectedValuesList();
	}
}
