package multisala.gui;

import java.util.List;

import multisala.core.Reservation;

public class AllReservationsPanel extends ReservationPanel {

	private AdminUI parent;
	
	public AllReservationsPanel(AdminUI parent) {
		super(parent);
		this.parent = parent;
	}
	
	@Override
	protected void updateView() {
		List<Reservation> res = parent.getAgent().getReservations();
		list.setModel(new ReservationTableModel(res));
	}

}
