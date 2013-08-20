package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public abstract class AbstractUI extends JFrame implements Runnable {

	protected JToolBar toolBar;
	protected JTabbedPane tabbedView;
	protected JLabel statusLabel;
	protected JLabel userLabel;
	
	protected final Dimension tabSize;
	
	protected AbstractUI() {
		super("Multisala");
		tabSize = new Dimension(640, 480);
		userLabel = new JLabel();
	}
	
	public void setStatus(String message) {
		statusLabel.setText(message);
	}
	
	@Override
	public void run() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		tabbedView = createTabbedView();
		toolBar = createToolBar();
		createStatusBar();
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		getContentPane().add(tabbedView, BorderLayout.CENTER);
		getContentPane().add(statusLabel.getParent(), BorderLayout.PAGE_END);
		pack();
		setVisible(true);
	}

	protected abstract JTabbedPane createTabbedView();

	protected abstract JToolBar createToolBar();
	
	protected void createStatusBar() {
		JPanel statusBar = new JPanel(new GridLayout(1,2));
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setPreferredSize(new Dimension(this.getContentPane().getWidth(), 24));
		statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.add(statusLabel);
		userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		statusBar.add(userLabel);
	}
}
