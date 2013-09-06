package multisala.gui;

import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import multisala.core.Reservation;

/**
 * Il modello per la tabella (JTable) che contiene un elenco di prenotazioni.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 */
public class ReservationTableModel extends AbstractTableModel implements Serializable {

	/**
	 * Le prenotazioni da mostrare.
	 */
	private Reservation[] reservations;
	
	/**
	 * I nomi delle colonne della tabella.
	 */
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
	
	/**
	 * @return Il numero di righe della tabella, corrispondente al numero di prenotazioni.
	 */
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
	
	/**
	 * Restituisce la prenotazione che nella tabella si trova all'indice di riga specificato.
	 * @param i l'indice della riga
	 * @return La prenotazione selezionata.
	 */
	public Reservation getReservationAtIndex(int i) {
		return reservations[i];
	}
}
