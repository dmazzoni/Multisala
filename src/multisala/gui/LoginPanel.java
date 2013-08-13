package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel {
	
	private GuestUI parent;
	
	private JTextField userField;
	private JPasswordField passField;
	private JLabel messageLabel;

	public LoginPanel(GuestUI parent) {
		this.parent = parent;
		this.setLayout(new BorderLayout());
		this.add(createHeader(), BorderLayout.PAGE_START); 
		this.add(createForm(), BorderLayout.CENTER);
		this.add(createButtonArea(), BorderLayout.PAGE_END);
		
	}
	
	private JLabel createHeader() {
		JLabel header = new JLabel("Login");
		header.setHorizontalAlignment(SwingConstants.CENTER);
		return header;
	}
	
	private JPanel createForm() {
		JPanel form = new JPanel(new FlowLayout());
		JPanel labels = new JPanel();
		labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
		JPanel fields = new JPanel();
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
		userField = new JTextField(30);
		passField = new JPasswordField(30);
		userField.setMaximumSize(new Dimension(120,24));
		passField.setMaximumSize(new Dimension(120,24));
		labels.add(new JLabel("Username:"));
		fields.add(userField);
		labels.add(new JLabel("Password:"));
		fields.add(passField);
		form.add(labels);
		form.add(fields);
		return form;
	}
	
	private JPanel createButtonArea() {
		JPanel buttonArea = new JPanel();
		buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.Y_AXIS));
		messageLabel = new JLabel("Text");
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JButton submit = new JButton("Accedi");
		submit.setAlignmentX(CENTER_ALIGNMENT);
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
								
			}
		});
		
		buttonArea.add(messageLabel);
		buttonArea.add(submit);
		return buttonArea;		
	}
}
