package multisala.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import multisala.core.IGuest;

public class GuestUI extends AbstractUI {
	
	private IGuest agent;

	public GuestUI(IGuest guestMA) {
		super();
		this.agent = guestMA;
		this.agent.setWindow(this);
	}
	
	public IGuest getAgent() {
		return agent;
	}

	@Override
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JButton loginButton = new JButton("Accedi");
		loginButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				LoginPanel lPanel = new LoginPanel(GuestUI.this);
				GuestUI.this.tabbedView.addTab("Accedi", lPanel);
				GuestUI.this.tabbedView.setSelectedComponent(lPanel);
				GuestUI.this.repaint();
            }                       
		});
		JButton regButton = new JButton("Registrati");
		regButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RegistrationPanel rPanel = new RegistrationPanel(GuestUI.this);
				GuestUI.this.tabbedView.addTab("Registrati", rPanel);
				GuestUI.this.tabbedView.setSelectedComponent(rPanel);
				GuestUI.this.repaint();
			}
			
		});
		toolBar.add(loginButton);
		toolBar.add(regButton);
		return toolBar;
	}
	
	@Override
	protected JTabbedPane createTabbedView() {
		JTabbedPane tabbedView = new JTabbedPane();
		tabbedView.setPreferredSize(tabSize);
		tabbedView.addTab("Programmazione", new GuestSchedulePanel(this));
		return tabbedView;
	}

}
