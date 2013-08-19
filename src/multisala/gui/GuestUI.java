package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import multisala.core.IGuest;

public class GuestUI extends AbstractUI {
	
	protected IGuest agent;

	public GuestUI(IGuest guestMA) {
		super();
		this.agent = guestMA;
		this.agent.setWindow(this);
	}
	
	/*protected GuestUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.statusLabel = createStatusBar();
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(statusLabel.getParent(), BorderLayout.PAGE_END);
	}*/
	
	@Override
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JButton loginButton = new JButton("Accedi");
		loginButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				GuestUI.this.tabbedView.addTab("Accedi", new LoginPanel(GuestUI.this));
				GuestUI.this.repaint();
            }                       
		});
		JButton regButton = new JButton("Registrati");
		regButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GuestUI.this.tabbedView.addTab("Registrati", new RegistrationPanel(GuestUI.this));
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
		tabbedView.setPreferredSize(new Dimension(600, 400));
		tabbedView.addTab("Programmazione", new GuestSchedulePanel(this));
		return tabbedView;
	}
	
	@Override
	protected JLabel createStatusBar() {
		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setPreferredSize(new Dimension(this.getContentPane().getWidth(), 24));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.add(statusLabel);
		return statusLabel;
	}

	public IGuest getAgent() {
		return agent;
	}

}
