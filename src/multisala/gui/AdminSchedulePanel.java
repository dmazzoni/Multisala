package multisala.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import multisala.core.Show;

/**
 * Il pannello che mostra la programmazione per l'amministratore.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 *
 */
public class AdminSchedulePanel extends UserSchedulePanel {

	/**
	 * La finestra che ospita il pannello.
	 */
	private AdminUI parent;
	
	
	/**
	 * Il menù contestuale per il click destro del mouse.
	 */
	private JPopupMenu popupMenu;
	
	/**
	 * Il campo di testo per il numero di biglietti da vendere.
	 */
	private JTextField seatsField;
	
	public AdminSchedulePanel(AdminUI parent) {
		super(parent);
		this.parent = parent;
		this.popupMenu = createPopupMenu();
		this.list.setComponentPopupMenu(popupMenu);
	}
	
	/**
	 * {@inheritDoc} <br>
	 * Per l'ammministratore mostra anche una barra inferiore che permette
	 * di effettuare la vendita diretta di biglietti di uno spettacolo selezionato.
	 */
	@Override
	protected void initView() {
		super.initView();
		JToolBar lowerToolbar = new JToolBar();
		lowerToolbar.setFloatable(false);
		lowerToolbar.add(new JLabel("Numero biglietti per lo spettacolo selezionato:"));
		lowerToolbar.addSeparator();
		seatsField = new JTextField();
		lowerToolbar.add(seatsField);
		JButton confirmSale = new JButton("Vendi");
		confirmSale.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selection = list.convertRowIndexToModel(list.getSelectedRow());
				Show sh = ((ScheduleTableModel) list.getModel()).getShowAtIndex(selection);
				int seats = Integer.parseInt(seatsField.getText());
				parent.getAgent().sellTickets(sh, seats);
				seatsField.setText("");
			}
			
		});
		lowerToolbar.add(confirmSale);
		panel.add(lowerToolbar);
	}
	
	/**
	 * Crea il menù contestuale con le opzioni per modificare o 
	 * eliminare uno spettacolo selezionato.
	 * @return Il menù creato.
	 */
	private JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem editMenuItem = new JMenuItem("Modifica");
		editMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] selection = list.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = list.convertRowIndexToModel(selection[i]);
					Show sh = ((ScheduleTableModel) list.getModel()).getShowAtIndex(selection[i]);
					ShowManagementPanel sPanel = new ShowManagementPanel(parent, sh);
					parent.tabbedView.addTab("Modifica spettacolo", sPanel);
					parent.tabbedView.setSelectedComponent(sPanel);
				}
			}
		});
		JMenuItem deleteMenuItem = new JMenuItem("Elimina");
		deleteMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] selection = list.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = list.convertRowIndexToModel(selection[i]);
					Show sh = ((ScheduleTableModel) list.getModel()).getShowAtIndex(selection[i]);
					parent.getAgent().deleteShow(sh.getId());
				}
			}
		});
		menu.add(editMenuItem);
		menu.add(deleteMenuItem);
		return menu;
	}
}
