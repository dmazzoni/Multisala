package multisala.gui;

import java.io.Serializable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import multisala.core.Show;

public class ScheduleTableModel extends AbstractTableModel implements Serializable {

	private Show[] shows;
	private final String[] colNames = {"Titolo", "Ora", "Sala"};
	
	public ScheduleTableModel(List<Show> shows) {
		this.shows = shows.toArray(new Show[shows.size()]);
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colNames[columnIndex];
	}
	
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
		}
		throw new IllegalArgumentException();
	}
	
	public Show getShowAtIndex(int i) {
		return shows[i];
	}
}

