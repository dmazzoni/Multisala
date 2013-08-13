package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class GuestUI extends JFrame implements Runnable {
	
	private IGuest guestMA;
	
	protected JToolBar toolBar;
	protected JTabbedPane tabbedView;
	protected JLabel statusLabel;

	public GuestUI(IGuest guestMA) {
		super("Multisala");
		this.guestMA = guestMA;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.toolBar = createToolBar();
		this.tabbedView = createTabbedView();
		this.statusLabel = createStatusBar();
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(toolBar, BorderLayout.PAGE_START); 
		pane.add(tabbedView, BorderLayout.CENTER);
		pane.add(statusLabel.getParent(), BorderLayout.PAGE_END);
		this.pack();
	}
	
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		final JButton loginButton = new JButton("Accedi");
		loginButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				LoginPanel loginPanel = new LoginPanel(GuestUI.this);
				GuestUI.this.getContentPane().remove(tabbedView);
				GuestUI.this.getContentPane().add(loginPanel, BorderLayout.CENTER);
				GuestUI.this.validate();
            }                       
		});
		toolBar.add(loginButton);
		return toolBar;
	}
	
	private JTabbedPane createTabbedView() {
		JTabbedPane tabbedView = new JTabbedPane();
		tabbedView.setPreferredSize(new Dimension(600, 400));
		return tabbedView;
	}
	
	private JLabel createStatusBar() {
		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setPreferredSize(new Dimension(this.getContentPane().getWidth(), 24));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.add(statusLabel);
		return statusLabel;
	}

	@Override
	public void run() {
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new GuestUI(null).run();
	}
}
