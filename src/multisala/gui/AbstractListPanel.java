package multisala.gui;

import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * Un generico pannello grafico che mostra un elenco.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public abstract class AbstractListPanel extends JPanel {
	
	/**
	 * La tabella contenente i dati da rappresentare.
	 */
	protected JTable list;
	
	protected AbstractListPanel() {
		list = new JTable();
	}
	
	protected abstract void updateView();

}
