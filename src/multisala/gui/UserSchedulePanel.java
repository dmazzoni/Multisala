package multisala.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.SwingUtilities;

import multisala.core.Show;

/**
 * Il pannello che mostra la programmazione per l'utente loggato.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 *
 */
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
			if (!isBeforeToday(scheduleDate)) {
				int selection = list.convertRowIndexToModel(list.getSelectedRow());
				if (selection >= 0) {
					Show sh = ((ScheduleTableModel) list.getModel()).getShowAtIndex(selection);
					ReservationManagementPanel rPanel = new ReservationManagementPanel(parent, this, sh);
					parent.tabbedView.addTab("Nuova prenotazione", rPanel);
					parent.tabbedView.setSelectedComponent(rPanel);
				}
			} else {
				parent.setStatus("Impossibile prenotare: spettacolo già passato");
			}
		}
	}
	
	/**
	 * Verifica se la data della programmazione visualizzata precede la data di oggi.
	 * @param scheduleDate la data della programmazione
	 * @return <code>true</code> se la data della programmazione precede quella odierna.
	 */
	private static boolean isBeforeToday(Calendar scheduleDate) {
		Calendar today = new GregorianCalendar();
		
		int month = today.get(Calendar.MONTH) + 1;
		String monthString = new String((month < 10 ? "0" : "") + month);
		int day = today.get(Calendar.DATE);
		String dayString = new String((day < 10 ? "0" : "") + day);
		String todayString = new String(today.get(Calendar.YEAR) + "-" + monthString + "-" +  dayString);
		
		month = scheduleDate.get(Calendar.MONTH) + 1;
		monthString = new String((month < 10 ? "0" : "") + month);
		day = scheduleDate.get(Calendar.DATE);
		dayString = new String((day < 10 ? "0" : "") + day);
		String scheduleDateString = new String(scheduleDate.get(Calendar.YEAR) + "-" + monthString + "-" +  dayString);	
		
		return scheduleDateString.compareTo(todayString) < 0;
	}
}

