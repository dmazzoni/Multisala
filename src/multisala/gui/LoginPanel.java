package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.security.auth.login.LoginException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import multisala.core.AbstractClient;

public class LoginPanel extends JPanel {
	
	private GuestUI parent;
	
	private JTextField userField;
	private JPasswordField passField;
	private JLabel messageLabel;

	public LoginPanel(GuestUI parent) {
		this.parent = parent;
		this.initView();
	}
	
	private void submitAction() {
		try {
			String username = userField.getText();
			AbstractClient loggedClient = parent.agent.login(username, new String(passField.getPassword()));
			if (loggedClient instanceof UserMA) {
				UserUI userUI = new UserUI(loggedClient, username);
				parent.setVisible(false);
				userUI.run();
			} else {
				AdminUI adminUI = new AdminUI(loggedClient, username);
				parent.setVisible(false);
				adminUI.run();
			}
			parent.dispose();
		} catch (LoginException e) {
			messageLabel.setText(e.getMessage());
		}
	}
	
	private void cancelAction() {
		userField.setText("");
		passField.setText("");
	}

	private void initView() {
		// Auto-generated code
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
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Dialog", Font.BOLD, 16));
		lblLogin.setAlignmentX(0.5f);
		panel.add(lblLogin);
		
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
		
		JButton btnSubmit = new JButton("Accedi");
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
