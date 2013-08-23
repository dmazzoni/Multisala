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
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import multisala.core.IAdmin;
import multisala.core.Show;
import java.awt.Color;

public class GuestSchedulePanel extends AbstractListPanel {
	
	private Calendar currentDate;
	private GuestUI parent;
	
	private JLabel lblScheduleDate;
	protected JPanel panel;
	
	public GuestSchedulePanel(GuestUI parent) {
		this.parent = parent;
		this.currentDate = new GregorianCalendar();
		this.lblScheduleDate = new JLabel();
		this.panel = new JPanel();
		updateView();
		initView();
	}
	
	@Override
	protected void updateView() {
		updateSchedule(0);
	}	

	protected void updateSchedule(int offset) {
		currentDate.add(Calendar.DAY_OF_MONTH, offset);
		String dtString = new String("Programmazione del " + currentDate.get(Calendar.DAY_OF_MONTH) + "/" + 
				(currentDate.get(Calendar.MONTH) + 1) + "/" + currentDate.get(Calendar.YEAR));
		lblScheduleDate.setText(dtString);
		List<Show> shows = parent.getAgent().getSchedule(currentDate);
		list.setModel(new ScheduleTableModel(shows));
	}
	
	protected void initView() {
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
		
		list.setBackground(new Color(238, 238, 238));
		list.setMaximumSize(new Dimension(32767, 32767));
		panel.add(list);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_3);
	}
	
}
