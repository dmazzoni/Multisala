package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import multisala.core.Show;

public class ShowManagementPanel extends JPanel {

	private AdminUI parentWindow;
	private AbstractListPanel parentTab;
	private Show sh;
	
	private JTextField titleField;
	private JTextField dateField;
	private JTextField timeField;
	private JTextField theaterField;
	private JTextField seatsField;
	private JLabel messageLabel;
	private JButton btnSubmit;
	
	public ShowManagementPanel(AdminUI parentWindow, AbstractListPanel parentTab) {
		this.parentWindow = parentWindow;
		this.parentTab = parentTab;
		initView();
	}
	
	public ShowManagementPanel(AdminUI parentWindow, AbstractListPanel parentTab, Show sh) {
		this(parentWindow, parentTab);
		this.sh = sh;
		titleField.setText(sh.getTitle());
		dateField.setText(sh.getDate());
		timeField.setText(sh.getTime());
		theaterField.setText(sh.getTheater());
		seatsField.setText(sh.getFreeSeats() + "");
	}
	
	public ShowManagementPanel(AdminUI parentWindow, AbstractListPanel parentTab, String showTitle) {
		this(parentWindow, parentTab);
		titleField.setText(showTitle);
	}

	private void submitShow() {
		try {
			int seats = Integer.parseInt(seatsField.getText());
			if (sh == null) {
				sh = new Show(0, titleField.getText(), dateField.getText(), timeField.getText(), theaterField.getText(), seats);
				parentWindow.getAgent().insertShow(sh);
			} else {
				int id = sh.getId();
				sh = new Show(id, titleField.getText(), dateField.getText(), timeField.getText(), theaterField.getText(), seats);
				parentWindow.getAgent().editShow(sh);
			}
			parentTab.updateView();
			parentWindow.tabbedView.setSelectedComponent(parentTab);
			parentWindow.tabbedView.remove(this);
		} catch (NumberFormatException e) {
			messageLabel.setText("Numero posti non valido");
		}
	}
	
	private void cancel() {
		parentWindow.tabbedView.setSelectedComponent(parentTab);
		parentWindow.tabbedView.remove(this);
	}
	
	private void initView() {
		setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1, BorderLayout.SOUTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		add(horizontalStrut_1, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel lblHeader = new JLabel("Spettacolo");
		lblHeader.setFont(new Font("Dialog", Font.BOLD, 16));
		lblHeader.setAlignmentX(0.5f);
		panel.add(lblHeader);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_2);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblTitle = new JLabel("Titolo");
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblTitle);
		
		titleField = new JTextField();
		titleField.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_1.add(titleField);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblDate = new JLabel("Data");
		lblDate.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblDate);
		
		dateField = new JTextField();
		dateField.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_2.add(dateField);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblTime = new JLabel("Ora");
		lblTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblTime.setFont(new Font("Dialog", Font.BOLD, 14));
		panel_4.add(lblTime);
		
		timeField = new JTextField();
		timeField.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_4.add(timeField);
		
		JPanel panel_7 = new JPanel();
		panel.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblTheater = new JLabel("Sala");
		lblTheater.setHorizontalAlignment(SwingConstants.CENTER);
		lblTheater.setFont(new Font("Dialog", Font.BOLD, 14));
		panel_7.add(lblTheater);
		
		theaterField = new JTextField();
		theaterField.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_7.add(theaterField);
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblSeats = new JLabel("Posti");
		lblSeats.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeats.setFont(new Font("Dialog", Font.BOLD, 14));
		panel_5.add(lblSeats);
		
		seatsField = new JTextField();
		seatsField.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_5.add(seatsField);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_3);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{47, 47, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		messageLabel = new JLabel("");
		messageLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_messageLabel = new GridBagConstraints();
		gbc_messageLabel.gridwidth = 2;
		gbc_messageLabel.fill = GridBagConstraints.VERTICAL;
		gbc_messageLabel.insets = new Insets(0, 0, 5, 0);
		gbc_messageLabel.gridx = 0;
		gbc_messageLabel.gridy = 0;
		panel_3.add(messageLabel, gbc_messageLabel);
		
		btnSubmit = new JButton("Salva");
		btnSubmit.setFont(new Font("Dialog", Font.BOLD, 14));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitShow();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		panel_3.add(btnSubmit, gbc_btnNewButton);
		
		JButton btnCancel = new JButton("Annulla");
		btnCancel.setFont(new Font("Dialog", Font.BOLD, 14));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.gridx = 1;
		gbc_btnCancel.gridy = 1;
		panel_3.add(btnCancel, gbc_btnCancel);
	}
}
