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

import multisala.core.Reservation;
import multisala.core.Show;
import multisala.exceptions.ReservationException;

public class ReservationManagementPanel extends JPanel {
	
	private AbstractListPanel parentTab;
	private UserUI parentWindow;
	
	private Reservation res;
	private Show sh;
	
	private JLabel TitleValue;
	private JLabel DateValue;
	private JTextField seatsField;
	private JLabel messageLabel;
	private JButton btnSubmit;

	public ReservationManagementPanel(UserUI parentWindow, AbstractListPanel parentTab, Reservation res) {
		this(parentWindow, parentTab, res.getShow());
		this.res = res;
	}
	
	public ReservationManagementPanel(UserUI parentWindow, AbstractListPanel parentTab, Show sh) {
		this.parentWindow = parentWindow;
		this.parentTab = parentTab;
		this.sh = sh;
		initView();
	}
	
	private void submitReservation() {
		try {
			int seats = Integer.parseInt(seatsField.getText());
			if (res == null) {
				res = new Reservation(0, sh, parentWindow.getUsername(), seats);
				parentWindow.getAgent().insertReservation(res);
				parentWindow.statusLabel.setText("Prenotazione effettuata con successo");
			} else {
				res.setSeats(seats);
				parentWindow.getAgent().editReservation(res);
				parentWindow.statusLabel.setText("Prenotazione aggiornata con successo");
			}
			parentWindow.tabbedView.setSelectedComponent(parentTab);
			parentWindow.tabbedView.remove(this);
		} catch (NumberFormatException e) {
			messageLabel.setText("Numero posti non valido");
		} catch (ReservationException e) {
			messageLabel.setText(e.getMessage());
		}
	}
	
	private void cancel() {
		parentWindow.tabbedView.setSelectedComponent(parentTab);
		parentWindow.tabbedView.remove(this);
	}
	
	public void initView() {
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
		
		JLabel lblHeader = new JLabel("Prenotazione");
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
		
		TitleValue = new JLabel();
		TitleValue.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_1.add(TitleValue);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblDate = new JLabel("Orario");
		lblDate.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblDate);
		
		DateValue = new JLabel();
		DateValue.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_2.add(DateValue);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblFreeSeats = new JLabel("Posti Liberi");
		lblFreeSeats.setHorizontalAlignment(SwingConstants.CENTER);
		lblFreeSeats.setFont(new Font("Dialog", Font.BOLD, 14));
		panel_4.add(lblFreeSeats);
		
		JLabel FreeSeatsValue = new JLabel();
		FreeSeatsValue.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_4.add(FreeSeatsValue);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_3);
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblReservationSeats = new JLabel("Numero posti da prenotare");
		lblReservationSeats.setHorizontalAlignment(SwingConstants.CENTER);
		lblReservationSeats.setFont(new Font("Dialog", Font.BOLD, 14));
		panel_5.add(lblReservationSeats);
		
		seatsField = new JTextField();
		seatsField.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_5.add(seatsField);
		
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
		
		btnSubmit = new JButton("Prenota");
		btnSubmit.setFont(new Font("Dialog", Font.BOLD, 14));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitReservation();
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
