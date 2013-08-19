package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import multisala.core.IAdmin;
import multisala.core.Show;

public class SchedulePanel<T extends GuestUI> extends JPanel {
	
	private Calendar currentDate;
	private JTable schedule;
	private T parent;
	
	private JLabel lblScheduleDate;
	
	public SchedulePanel(T parent) {
		this.parent = parent;
		this.currentDate = new GregorianCalendar();
		this.lblScheduleDate = new JLabel();
		this.schedule = new JTable();
		updateSchedule(0);

		this.schedule.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				/*if(SwingUtilities.isRightMouseButton(e) && SchedulePanel.this.parent instanceof AdminUI) {
					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem editMenuItem = new JMenuItem("Modifica");
					editMenuItem.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							int[] selection = schedule.getSelectedRows();
							for (int i = 0; i < selection.length; i++) {
								selection[i] = schedule.convertRowIndexToModel(selection[i]);
								Show sh = ((ScheduleTableModel) schedule.getModel()).getShowAtIndex(selection[i]);
								SchedulePanel.this.parent.tabbedView.add(new ShowPanel(SchedulePanel.this.parent, sh));
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
								((IAdmin) SchedulePanel.this.parent.agent).deleteShow(sh.getId());
							}
						}
					});
					return;
				}
				if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2 && SchedulePanel.this.parent instanceof UserUI) {
					int selection = schedule.convertRowIndexToModel(schedule.getSelectedRow());
					Show sh = ((ScheduleTableModel) schedule.getModel()).getShowAtIndex(selection);
					SchedulePanel.this.parent.tabbedView.add(new ReservationPanel(SchedulePanel.this.parent, sh));
				}
			*/}
		});
		initView();
	}

	private void updateSchedule(int offset) {
		currentDate.add(Calendar.DAY_OF_MONTH, offset);
		String dtString = new String("Programmazione del " + currentDate.get(Calendar.DAY_OF_MONTH) + "/" + 
				(currentDate.get(Calendar.MONTH) + 1) + "/" + currentDate.get(Calendar.YEAR));
		lblScheduleDate.setText(dtString);
		List<Show> shows = parent.agent.getSchedule(currentDate);
		schedule.setModel(new ScheduleTableModel(shows));
	}
	
	private void initView() {
		setLayout(new BorderLayout(0, 0));
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		add(horizontalStrut_1, BorderLayout.EAST);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut, BorderLayout.SOUTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{116, 222, 111, 0};
		gbl_panel_1.rowHeights = new int[]{25, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btnPrevious = new JButton("Precedente");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSchedule(-1);
			}
		});
		GridBagConstraints gbc_btnPrevious = new GridBagConstraints();
		gbc_btnPrevious.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPrevious.insets = new Insets(0, 0, 0, 5);
		gbc_btnPrevious.gridx = 0;
		gbc_btnPrevious.gridy = 0;
		panel_1.add(btnPrevious, gbc_btnPrevious);
		
		lblScheduleDate.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblScheduleDate = new GridBagConstraints();
		gbc_lblScheduleDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblScheduleDate.insets = new Insets(0, 0, 0, 5);
		gbc_lblScheduleDate.gridx = 1;
		gbc_lblScheduleDate.gridy = 0;
		panel_1.add(lblScheduleDate, gbc_lblScheduleDate);
		
		JButton btnNext = new JButton("Successivo");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSchedule(1);
			}
		});
		btnNext.setSize(new Dimension(130, 24));
		GridBagConstraints gbc_btnNext = new GridBagConstraints();
		gbc_btnNext.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNext.gridx = 2;
		gbc_btnNext.gridy = 0;
		panel_1.add(btnNext, gbc_btnNext);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_2);
		
		panel.add(schedule);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_3);
	}
	
	private class ScheduleTableModel extends AbstractTableModel {

		private Show[] shows;
		private final String[] colNames = {"Titolo", "Ora", "Sala", "Posti liberi"};
		
		public ScheduleTableModel(List<Show> shows) {
			this.shows = shows.toArray(new Show[shows.size()]);
		}
		
		@Override
		public int getColumnCount() {
			return 4;
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
				case 3: return shows[row].getFreeSeats();
			}
			throw new IllegalArgumentException();
		}
		
		public Show getShowAtIndex(int i) {
			return shows[i];
		}
	}
}
