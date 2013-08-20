package multisala.gui;

import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import multisala.core.Reservation;
import multisala.core.Show;

public class ReservationTableModel extends AbstractTableModel implements Serializable {

	private Reservation[] reservations;
	private final String[] colNames = {"Codice", "Utente", "Titolo", "Data", "Ora", "Posti"};
	
	public ReservationTableModel(List<Reservation> reservations) {
		this.reservations = reservations.toArray(new Reservation[reservations.size()]);
	}
	
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colNames[columnIndex];
	}
	
	@Override
	public int getRowCount() {
		return reservations.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0: return reservations[row].getId();
			case 1: return reservations[row].getUser();
			case 2: return reservations[row].getShow().getTitle();
			case 3: return reservations[row].getShow().getDate();
			case 4: return reservations[row].getShow().getTime();
			case 5: return reservations[row].getSeats();
		}
		throw new IllegalArgumentException();
	}
	
	public Reservation getReservationAtIndex(int i) {
		return reservations[i];
	}
}
