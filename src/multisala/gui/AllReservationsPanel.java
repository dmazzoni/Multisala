package multisala.gui;

import java.util.List;

import multisala.core.Reservation;

/**
 * Il pannello che mostra all'amministratore l'elenco di tutte
 * le prenotazioni che sono presenti nel sistema.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 *
 */
public class AllReservationsPanel extends ReservationPanel {

	/**
	 * La finestra che ospita il pannello.
	 */
	private AdminUI parent;
	
	public AllReservationsPanel(AdminUI parent) {
		super(parent);
		this.parent = parent;
	}
	
	@Override
	public void updateView() {
		List<Reservation> res = parent.getAgent().getAllReservations();
		list.setModel(new ReservationTableModel(res));
	}

}
