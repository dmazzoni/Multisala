package multisala.gui;

import javax.swing.JPanel;
import javax.swing.JTable;

public abstract class AbstractListPanel extends JPanel {
	
	protected JTable list;
	
	protected AbstractListPanel() {
		list = new JTable();
	}
	
	protected abstract void updateView();

}
