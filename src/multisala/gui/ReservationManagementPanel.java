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

/**
 * Il form di gestione di una prenotazione.
 * Permette l'inserimento dei dati di una nuova prenotazione per un dato
 * spettacolo, o la modifica dei dati di una prenotazione esistente.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 */
public class ReservationManagementPanel extends JPanel {
	
	/**
	 * Il pannello da cui si è generato il form per la prenotazione.
	 */
	private AbstractListPanel parentTab;
	
	/**
	 * La finestra che ospita il pannello.
	 */
	private UserUI parentWindow;
	
	
	/**
	 * La prenotazione oggetto della modifica.
	 */
	private Reservation res;
	
	/**
	 * Lo spettacolo di cui si sta effettuando la prenotazione.
	 */
	private Show sh;
	
	/**
	 * Il titolo dello spettacolo.
	 */
	private JLabel titleValue;
	
	/**
	 * Data e ora dello spettacolo.
	 */
	private JLabel dateValue;
	
	/**
	 * Campo per l'inserimento del numero di posti.
	 */
	private JTextField seatsField;
	
	/**
	 * Messaggio informativo in caso di errori.
	 */
	private JLabel messageLabel;
	
	/**
	 * Pulsante per confermare la modifica o l'inserimento della prenotazione.
	 */
	private JButton btnSubmit;

	public ReservationManagementPanel(UserUI parentWindow, AbstractListPanel parentTab, Reservation res) {
		this(parentWindow, parentTab, res.getShow());
		this.res = res;
		seatsField.setText(res.getSeats() + "");
	}
	
	public ReservationManagementPanel(UserUI parentWindow, AbstractListPanel parentTab, Show sh) {
		this.parentWindow = parentWindow;
		this.parentTab = parentTab;
		this.sh = sh;
		initView();
		titleValue.setText(sh.getTitle());
		dateValue.setText(sh.getDate() + " " + sh.getTime());
	}
	
	/**
	 * Conferma le modifiche apportate e:
	 * <li> inserisce la nuova prenotazione se il pannello originario era quello 
	 * della programmazione (ovvero se nessuna prenotazione è stata passata al costruttore)
	 * <li> aggiorna i dati della prenotazione specificata se il pannello originario 
	 * era quello delle prenotazioni
	 */
	private void submitReservation() {
		try {
			int seats = Integer.parseInt(seatsField.getText());
			if (res == null) {
				res = new Reservation(0, sh, parentWindow.getAgent().getUsername(), seats);
				parentWindow.getAgent().insertReservation(res);
				parentWindow.setStatus("Prenotazione effettuata con successo");
			} else {
				res.setSeats(seats);
				parentWindow.getAgent().editReservation(res);
				parentWindow.setStatus("Prenotazione aggiornata con successo");
			}
			parentWindow.tabbedView.setSelectedComponent(parentTab);
			parentWindow.tabbedView.remove(this);
		} catch (NumberFormatException e) {
			messageLabel.setText("Numero posti non valido");
		} catch (ReservationException e) {
			messageLabel.setText(e.getMessage());
		}
	}
	
	/**
	 * Chiude la scheda di modifica o inserimento della prenotazione senza
	 * salvare i cambiamenti apportati.
	 */
	private void cancel() {
		parentWindow.tabbedView.setSelectedComponent(parentTab);
		parentWindow.tabbedView.remove(this);
	}
	
	/**
	 * Setta le proprietà dei componenti grafici e li dispone sul pannello.
	 */
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
		
		titleValue = new JLabel();
		titleValue.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_1.add(titleValue);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblDate = new JLabel("Orario");
		lblDate.setFont(new Font("Dialog", Font.BOLD, 14));
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblDate);
		
		dateValue = new JLabel();
		dateValue.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_2.add(dateValue);
		
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
