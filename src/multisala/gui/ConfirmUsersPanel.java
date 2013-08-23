package multisala.gui;

import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;

public class ConfirmUsersPanel extends JPanel {
	
	List<String> users;
	
	JList<String> confirmationList;

	public ConfirmUsersPanel(List<String> users) {
		this.users = users;
		this.confirmationList = new JList<String>(users.toArray(new String[users.size()]));
	}

	public List<String> getConfirmedUsers() {
		return confirmationList.getSelectedValuesList();
	}
}
