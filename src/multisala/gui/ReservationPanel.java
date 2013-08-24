package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import multisala.core.Reservation;

public class ReservationPanel extends AbstractListPanel {
	
	private UserUI parent;
	
	private JPopupMenu popupMenu;

	public ReservationPanel(UserUI parent) {
		this.parent = parent;
		this.popupMenu = createPopupMenu();
		this.list.setComponentPopupMenu(popupMenu);
		updateView();
		initView();
	}
	
	@Override
	protected void updateView() {
		List<Reservation> res = parent.getAgent().getReservations();
		list.setModel(new ReservationTableModel(res));
	}
	
	private JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem editMenuItem = new JMenuItem("Modifica");
		editMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] selection = list.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = list.convertRowIndexToModel(selection[i]);
					Reservation res = ((ReservationTableModel) list.getModel()).getReservationAtIndex(selection[i]);
					ReservationManagementPanel rPanel = new ReservationManagementPanel(parent, ReservationPanel.this, res);
					parent.tabbedView.addTab("Modifica prenotazione", rPanel);
					parent.tabbedView.setSelectedComponent(rPanel);
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
					Reservation res = ((ReservationTableModel) list.getModel()).getReservationAtIndex(selection[i]);
					parent.getAgent().deleteReservation(res.getId());
				}
				updateView();
			}
		});
		menu.add(editMenuItem);
		menu.add(deleteMenuItem);
		return menu;
	}
	
	private void initView() {
		JPanel panel = new JPanel();
		
		setLayout(new BorderLayout(0, 0));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		add(horizontalStrut_1, BorderLayout.EAST);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut, BorderLayout.SOUTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1, BorderLayout.NORTH);
		
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(32767, 30));
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton btnCloseTab = new JButton("Chiudi");
		btnCloseTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReservationPanel.this.parent.tabbedView.remove(ReservationPanel.this);
			}
		});
		
		panel_1.add(btnCloseTab);
		
		list.setBackground(new Color(238, 238, 238));
		list.setMaximumSize(new Dimension(32767, 32767));
		panel.add(new JScrollPane(list));
	}

}
