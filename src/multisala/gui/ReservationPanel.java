package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import multisala.core.Reservation;
import multisala.core.Show;

public class ReservationPanel extends JPanel {
	
	private UserUI parent;
	protected JTable reservations;
	
	private JPopupMenu popupMenu;

	public ReservationPanel(UserUI parent) {
		this.parent = parent;
		this.reservations = new JTable();
		this.popupMenu = createPopupMenu();
		reservations.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
					handleClick(e);
			}
		});
		updateReservations();
		initView();
	}
	
	protected void updateReservations() {
		List<Reservation> res = parent.getAgent().getReservations(parent.getUsername());
		reservations.setModel(new ReservationTableModel(res));
	}
	
	private void handleClick(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			popupMenu.setVisible(true);
		}
	}
	
	private JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem editMenuItem = new JMenuItem("Modifica");
		editMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] selection = reservations.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = reservations.convertRowIndexToModel(selection[i]);
					Reservation res = ((ReservationTableModel) reservations.getModel()).getReservationAtIndex(selection[i]);
					parent.tabbedView.addTab("Modifica prenotazione", new ReservationManagementPanel(parent, res));
				}
			}
		});
		JMenuItem deleteMenuItem = new JMenuItem("Elimina");
		deleteMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] selection = reservations.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					selection[i] = reservations.convertRowIndexToModel(selection[i]);
					Reservation res = ((ReservationTableModel) reservations.getModel()).getReservationAtIndex(selection[i]);
					parent.getAgent().deleteReservation(res.getId());
				}
				updateReservations();
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
		
		reservations.setBackground(new Color(238, 238, 238));
		reservations.setMaximumSize(new Dimension(32767, 32767));
		panel.add(reservations);
	}
	
	

}
