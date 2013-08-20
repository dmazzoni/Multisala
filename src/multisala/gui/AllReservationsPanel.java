package multisala.gui;

import java.util.List;

import multisala.core.Reservation;

public class AllReservationsPanel extends ReservationPanel {

	private AdminUI parent;
	
	public AllReservationsPanel(AdminUI parent) {
		super(parent);
		this.parent = parent;
		updateReservations();
	}
	
	@Override
	protected void updateReservations() {
		List<Reservation> res = parent.getAgent().getReservations();
		reservations.setModel(new ReservationTableModel(res));
	}

}
