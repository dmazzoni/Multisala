package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Component;
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

public class GuestUI extends AbstractUI {
	
	protected IGuest agent;

	public GuestUI(IGuest guestMA) {
		this();
		this.agent = guestMA;
		this.toolBar = createToolBar();
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
		this.pack();
	}
	
	protected GuestUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.tabbedView = createTabbedView();
		this.statusLabel = createStatusBar();
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(tabbedView, BorderLayout.CENTER);
		pane.add(statusLabel.getParent(), BorderLayout.PAGE_END);
	}
	
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		JButton loginButton = new JButton("Accedi");
		loginButton.addActionListener(new ActionListener() {

			@Override
            public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) GuestUI.this.getContentPane().getLayout();
				Component currentView = layout.getLayoutComponent(BorderLayout.CENTER);
				if (currentView instanceof LoginPanel) {
					GuestUI.this.getContentPane().remove(currentView);
					GuestUI.this.getContentPane().add(tabbedView, BorderLayout.CENTER);
					((JButton) GuestUI.this.toolBar.getComponentAtIndex(0)).setText("Accedi");
				} else {
					LoginPanel loginPanel = new LoginPanel(GuestUI.this);
					GuestUI.this.getContentPane().remove(tabbedView);
					GuestUI.this.getContentPane().add(loginPanel, BorderLayout.CENTER);
					((JButton) GuestUI.this.toolBar.getComponentAtIndex(0)).setText("Indietro");	
				}
				GuestUI.this.repaint();
            }                       
		});
		toolBar.add(loginButton);
		return toolBar;
	}
	
	private JTabbedPane createTabbedView() {
		JTabbedPane tabbedView = new JTabbedPane();
		tabbedView.setPreferredSize(new Dimension(600, 400));
		tabbedView.add(new SchedulePanel<GuestUI>(this));
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

	public IGuest getAgent() {
		return agent;
	}
	
	public static void main(String[] args) {
		new GuestUI(null).run();
	}

}
