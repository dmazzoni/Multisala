package multisala.gui;

import javax.security.auth.login.AccountException;

/**
 * Il pannello con il form per effettuare la richiesta di registrazione.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 *
 */
public class RegistrationPanel extends LoginPanel {

	public RegistrationPanel(GuestUI parent) {
		super(parent);
		lblHeader.setText("Registrazione");
		btnSubmit.setText("Richiedi");
	}
	
	/**
	 * Effettua la richiesta di registrazione invocando l'apposito metodo 
	 * dell'agente mobile.<br>
	 * In caso di esito positivo mostra un messaggio di conferma nella 
	 * barra di stato; in caso negativo mostra una stringa di spiegazione.
	 */
	@Override
	protected void submitAction() {
		String username = userField.getText();
		try {
			parent.getAgent().register(username, new String(passField.getPassword()));
			parent.setStatus("Richiesta di registrazione effettuata con successo");
		} catch (AccountException e) {
			messageLabel.setText(e.getMessage());
		}
		
	}
}
