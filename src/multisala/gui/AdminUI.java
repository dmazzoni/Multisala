package multisala.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import multisala.core.AdminMS;
import multisala.core.IAdmin;
import multisala.core.IGuest;

public class AdminUI extends UserUI {

	private AdminMS agent;
	
	public AdminUI(AdminMS agent) throws RemoteException {
		super(agent);
		this.agent = agent;
		UnicastRemoteObject.exportObject(agent, 6000);
	}
	
	@Override
	public IAdmin getAgent() {
		return agent;
	}
	
	@Override
	protected JTabbedPane createTabbedView() {
		JTabbedPane tabbedView = new JTabbedPane();
		tabbedView.setPreferredSize(tabSize);
		tabbedView.addTab("Programmazione", new AdminSchedulePanel(this));
		return tabbedView;
	}
	
	@Override
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setLayout(new GridLayout(1, 4));
		JButton resButton = new JButton("Mie prenotazioni");
		resButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				ReservationPanel rPanel = new ReservationPanel(AdminUI.this);
				AdminUI.this.tabbedView.addTab("Prenotazioni", rPanel);
				AdminUI.this.tabbedView.setSelectedComponent(rPanel);
				AdminUI.this.repaint();
            }                       
		});
		JButton allResButton = new JButton("Tutte le prenotazioni");
		allResButton.addActionListener(new ActionListener() {
			
			@Override
            public void actionPerformed(ActionEvent e) {
				AllReservationsPanel rPanel = new AllReservationsPanel(AdminUI.this);
				AdminUI.this.tabbedView.addTab("Prenotazioni", rPanel);
				AdminUI.this.tabbedView.setSelectedComponent(rPanel);
				AdminUI.this.repaint();
            }            
		});
		JButton insertShowButton = new JButton("Nuovo spettacolo");
		insertShowButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ShowManagementPanel sPanel = new ShowManagementPanel(AdminUI.this);
				AdminUI.this.tabbedView.addTab("Nuovo spettacolo", sPanel);
				AdminUI.this.tabbedView.setSelectedComponent(sPanel);
				AdminUI.this.repaint();
			}
			
		});
		JButton logoutButton = new JButton("Esci");
		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IGuest guestAgent = AdminUI.this.agent.logout();
				GuestUI guestUI = new GuestUI(guestAgent);
				AdminUI.this.setVisible(false);
				guestUI.run();
				AdminUI.this.dispose();
			}
			
		});
		resButton.setAlignmentX(LEFT_ALIGNMENT);
		logoutButton.setAlignmentX(RIGHT_ALIGNMENT);
		toolBar.add(resButton);
		toolBar.add(allResButton);
		toolBar.add(insertShowButton);
		toolBar.add(logoutButton);
		return toolBar;
	}
}
