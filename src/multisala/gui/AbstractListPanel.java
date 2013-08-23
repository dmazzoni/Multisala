package multisala.gui;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTable;

public abstract class AbstractListPanel extends JPanel {
	
	protected JTable list;
	
	protected AbstractListPanel() {
		list = new JTable();
	}
	
	protected abstract void updateView();
	
	protected void setSelectedItems(MouseEvent e) {
		int selectedRow = list.rowAtPoint(e.getPoint());
		if (selectedRow >= 0)
			list.getSelectionModel().addSelectionInterval(selectedRow, selectedRow);
	}

}
