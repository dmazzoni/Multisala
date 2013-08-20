package multisala.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import multisala.core.IGuest;
import multisala.core.IUser;

public class UserUI extends GuestUI {

	protected IUser agent;
	private String username;

	public UserUI(IUser agent, String username) {
		super(agent);
		this.agent = agent;
		this.username = username;
		this.userLabel.setText("Benvenuto, " + username);
	}
	
	@Override
	protected JTabbedPane createTabbedView() {
		JTabbedPane tabbedView = new JTabbedPane();
		tabbedView.setPreferredSize(tabSize);
		tabbedView.addTab("Programmazione", new UserSchedulePanel(this));
		return tabbedView;
	}

	@Override
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setLayout(new GridLayout(1, 2));
		JButton resButton = new JButton("Mie prenotazioni");
		resButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				//UserUI.this.tabbedView.addTab("Prenotazioni", new ReservationPanel(UserUI.this));
				UserUI.this.repaint();
            }                       
		});
		JButton logoutButton = new JButton("Esci");
		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IGuest guestAgent = UserUI.this.agent.logout();
				GuestUI guestUI = new GuestUI(guestAgent);
				UserUI.this.setVisible(false);
				guestUI.run();
				UserUI.this.dispose();
			}
			
		});
		resButton.setAlignmentX(LEFT_ALIGNMENT);
		logoutButton.setAlignmentX(RIGHT_ALIGNMENT);
		toolBar.add(resButton);
		toolBar.add(logoutButton);
		return toolBar;
	}

}
