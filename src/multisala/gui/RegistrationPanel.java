package multisala.gui;

import javax.security.auth.login.AccountException;

public class RegistrationPanel extends LoginPanel {

	public RegistrationPanel(GuestUI parent) {
		super(parent);
		lblHeader.setText("Registrazione");
		btnSubmit.setText("Richiedi");
	}
	
	@Override
	protected void submitAction() {
		String username = userField.getText();
		try {
			parent.agent.register(username, new String(passField.getPassword()));
		} catch (AccountException e) {
			messageLabel.setText(e.getMessage());
		}
		
	}
}
