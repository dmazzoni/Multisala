package multisala.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

public abstract class AbstractUI extends JFrame implements Runnable {

	protected JToolBar toolBar;
	protected JTabbedPane tabbedView;
	protected JLabel statusLabel;
	
	protected AbstractUI() {
		super("Multisala");
	}
	
	public void setStatus(String message) {
		statusLabel.setText(message);
	}
	
	@Override
	public void run() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		statusLabel = createStatusBar();
		statusLabel = createStatusBar();
		tabbedView = createTabbedView();
		toolBar = createToolBar();
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		getContentPane().add(tabbedView, BorderLayout.CENTER);
		getContentPane().add(statusLabel.getParent(), BorderLayout.PAGE_END);
		pack();
		setVisible(true);
	}

	protected abstract JLabel createStatusBar();

	protected abstract JTabbedPane createTabbedView();

	protected abstract JToolBar createToolBar();
	
}
