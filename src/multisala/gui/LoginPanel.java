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
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import multisala.core.AbstractAgent;
import multisala.core.AdminMS;
import multisala.core.IUser;

/**
 * Il pannello con il form per effettuare il login.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 *
 */
public class LoginPanel extends JPanel {
	
	/**
	 * La finestra che ospita il pannello.
	 */
	protected GuestUI parent;
	
	/**
	 * Il campo dove viene inserito il nome utente.
	 */
	protected JTextField userField;
	/**
	 * Il campo dove viene inserita la password.
	 */
	protected JPasswordField passField;
	/**
	 * Messaggio di avvertimento del fallimento del login.
	 */
	protected JLabel messageLabel;
	/**
	 * L'intestazione del form.
	 */
	protected JLabel lblHeader;
	/**
	 * Il pulsante di conferma.
	 */
	protected JButton btnSubmit;

	public LoginPanel(GuestUI parent) {
		this.parent = parent;
		this.initView();
	}
	
	/**
	 * Effettua il login invocando l'apposito metodo dell'agente mobile.
	 * In caso di esito positivo chiude l'attuale grafica dell'utente ospite e apre
	 * quella dell'utente registrato o dell'amministratore, a seconda del tipo
	 * di registrazione di tale utente.<br>
	 * In caso di esito negativo, mostra una stringa di spiegazione o apre una nuova
	 * finestra di avviso mostrando il messaggio dell'eccezione remota.
	 */
	protected void submitAction() {
		try {
			String username = userField.getText();
			AbstractAgent loggedClient = parent.getAgent().login(username, new String(passField.getPassword()));
			if (loggedClient instanceof AdminMS) {
				AdminUI adminUI = new AdminUI((AdminMS) loggedClient);
				parent.setVisible(false);
				adminUI.run();
			} else if (loggedClient instanceof IUser) {
				UserUI userUI = new UserUI((IUser) loggedClient);
				parent.setVisible(false);
				userUI.run();
			}
			if (loggedClient != null)
				parent.dispose();
		} catch (LoginException e) {
			messageLabel.setText(e.getMessage());
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(parent, e);
		}
	}
	
	/**
	 * Rimuove la scheda corrente.
	 */
	private void cancelAction() {
		parent.tabbedView.remove(this);
		parent.repaint();
	}

	/**
	 * Setta le proprietà dei componenti grafici e li dispone sul pannello.
	 */
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
		
		lblHeader = new JLabel("Login");
		lblHeader.setFont(new Font("Dialog", Font.BOLD, 16));
		lblHeader.setAlignmentX(0.5f);
		panel.add(lblHeader);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_2);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Dialog", Font.BOLD, 14));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblUsername);
		
		userField = new JTextField();
		userField.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_1.add(userField);
		userField.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 14));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblPassword);
		
		passField = new JPasswordField();
		passField.setFont(new Font("Dialog", Font.PLAIN, 14));
		passField.setColumns(10);
		panel_2.add(passField);
		
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
		
		btnSubmit = new JButton("Accedi");
		btnSubmit.setFont(new Font("Dialog", Font.BOLD, 14));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPanel.this.submitAction();
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
				LoginPanel.this.cancelAction();
			}
		});
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.gridx = 1;
		gbc_btnCancel.gridy = 1;
		panel_3.add(btnCancel, gbc_btnCancel);
	}
	
}
