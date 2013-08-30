package multisala.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import multisala.core.Show;

public class UserSchedulePanel extends GuestSchedulePanel {

	/**
	 * La finestra che ospita il pannello.
	 */
	private UserUI parent;
	
	public UserSchedulePanel(UserUI parent) {
		super(parent);
		this.parent = parent;
		list.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
					handleClick(e);
			}
		});
	}
	
	/**
	 * Riconosce il doppio click del tasto sinistro del mouse e apre
	 * una scheda per la prenotazione di biglietti per lo spettacolo 
	 * selezionato.
	 * @param e l'evento del mouse
	 */
	protected void handleClick(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
			int selection = list.convertRowIndexToModel(list.getSelectedRow());
			if (selection >= 0) {
				Show sh = ((ScheduleTableModel) list.getModel()).getShowAtIndex(selection);
				ReservationManagementPanel rPanel = new ReservationManagementPanel(parent, this, sh);
				parent.tabbedView.addTab("Nuova prenotazione", rPanel);
				parent.tabbedView.setSelectedComponent(rPanel);
			}
		}
	}
}

