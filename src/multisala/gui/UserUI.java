package multisala.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import multisala.core.IGuest;
import multisala.core.IUser;

/**
 * La finestra grafica dell'utente loggato.
 * @author Davide Mazzoni 
 * @author Giacomo Annaloro
 *
 */
public class UserUI extends GuestUI {

	/**
	 * L'agente mobile dell'utente loggato, collegato alla corrente vista grafica.
	 */
	private IUser agent;

	public UserUI(IUser agent) {
		super(agent);
		this.agent = agent;
		this.userLabel.setText("Benvenuto, " + agent.getUsername());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IUser getAgent() {
		return agent;
	}

	/**
	 * Crea il pannello a schede e mostra il tab della 
	 * programmazione con le opzioni disponibili per il tipo di utente registrato.
	 * @return La vista a schede.
	 */
	@Override
	protected JTabbedPane createTabbedView() {
		JTabbedPane tabbedView = new JTabbedPane();
		tabbedView.setPreferredSize(tabSize);
		tabbedView.addTab("Programmazione", new UserSchedulePanel(this));
		return tabbedView;
	}
	
	/**
	 * Crea la barra degli strumenti con i pulsanti per visualizzare le 
	 * prenotazioni effettuate e fare logout.
	 * @return La toolbar.
	 */
	@Override
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setLayout(new GridLayout(1, 2));
		JButton resButton = new JButton("Mie prenotazioni");
		resButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				ReservationPanel rPanel = new ReservationPanel(UserUI.this);
				rPanel.updateView();
				UserUI.this.tabbedView.addTab("Prenotazioni", rPanel);
				UserUI.this.tabbedView.setSelectedComponent(rPanel);
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
