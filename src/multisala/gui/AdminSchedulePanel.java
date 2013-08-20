package multisala.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import multisala.core.Show;

public class AdminSchedulePanel extends UserSchedulePanel {

	/*private AdminUI parent;
	
	private JPopupMenu popupMenu;
	private JTextField seatsField;
	
	public AdminSchedulePanel(AdminUI parent) {
		super();
		this.parent = parent;
		popupMenu = createPopupMenu();
		seatsField = new JTextField();
		updateSchedule(0);
	}
	
	@Override
	protected void handleClick(MouseEvent e) {
		super.handleClick(e);
		if(SwingUtilities.isRightMouseButton(e)) {
			popupMenu.setVisible(true);
		}
	}
	
	@Override
	protected void initView() {
		super.initView();
		JToolBar lowerToolbar = new JToolBar();
		lowerToolbar.add(new JLabel("Numero biglietti per lo spettacolo selezionato:"));
		lowerToolbar.addSeparator();
		lowerToolbar.add(seatsField);
		JButton confirmSale = new JButton("Vendi");
		confirmSale.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selection = schedule.convertRowIndexToModel(schedule.getSelectedRow());
				Show sh = ((ScheduleTableModel) schedule.getModel()).getShowAtIndex(selection);
				int seats = Integer.parseInt(seatsField.getText());
				parent.agent.sellTickets(sh, seats);
				seatsField.setText("");
			}
			
		});
		
	}
	
	private JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem editMenuItem = new JMenuItem("Modifica");
		editMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] selection = schedule.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = schedule.convertRowIndexToModel(selection[i]);
					Show sh = ((ScheduleTableModel) schedule.getModel()).getShowAtIndex(selection[i]);
					parent.tabbedView.add(new ShowManagementPanel(parent, sh));
				}
			}
		});
		JMenuItem deleteMenuItem = new JMenuItem("Elimina");
		deleteMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] selection = schedule.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = schedule.convertRowIndexToModel(selection[i]);
					Show sh = ((ScheduleTableModel) schedule.getModel()).getShowAtIndex(selection[i]);
					parent.agent.deleteShow(sh.getId());
				}
			}
		});
		menu.add(editMenuItem);
		menu.add(deleteMenuItem);
		return menu;
	}*/
}
