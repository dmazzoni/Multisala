package multisala.gui;

import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import multisala.core.Show;

/**
 * Il modello per la tabella (JTable) che contiene i dati della programmazione.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 */
public class ScheduleTableModel extends AbstractTableModel implements Serializable {

	/**
	 * Gli spettacoli da mostrare.
	 */
	private Show[] shows;
	
	/**
	 * I nomi delle colonne della tabella.
	 */
	private final String[] colNames = {"Titolo", "Ora", "Sala", "Posti liberi"};
	
	public ScheduleTableModel(List<Show> shows) {
		this.shows = shows.toArray(new Show[shows.size()]);
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
	 * @return Il numero di righe della tabella, corrispondente al numero di spettacoli.
	 */
	@Override
	public int getRowCount() {
		return shows.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
			case 0: return shows[row].getTitle();
			case 1: return shows[row].getTime();
			case 2: return shows[row].getTheater();
			case 3: return shows[row].getFreeSeats();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Restituisce lo spettacolo che nella tabella si trova all'indice di riga specificato.
	 * @param i l'indice della riga
	 * @return Lo spettacolo selezionato.
	 */
	public Show getShowAtIndex(int i) {
		return shows[i];
	}
}

