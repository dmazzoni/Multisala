package multisala.gui;

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
		this.setVisible(true);
	}
}
