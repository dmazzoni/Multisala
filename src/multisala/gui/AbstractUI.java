package multisala.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Generica finestra grafica contenente le parti comuni
 * a tutte le finestre.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 */
public abstract class AbstractUI extends JFrame implements Runnable {

	/**
	 * La barra degli strumenti.
	 */
	protected JToolBar toolBar;
	
	/**
	 * Il pannello a schede.
	 */
	protected JTabbedPane tabbedView;
	
	/**
	 * L'etichetta della barra di stato.
	 */
	private JLabel statusLabel;
	
	/**
	 * L'etichetta identificativa dell'utente attualmente connesso.
	 */
	protected JLabel userLabel;
	
	/**
	 * Le dimensioni predefinite delle schede.
	 */
	protected final Dimension tabSize;
	
	protected AbstractUI() {
		super("Multisala");
		tabSize = new Dimension(640, 480);
		userLabel = new JLabel();
	}
	
	/**
	 * Setta un nuovo messaggio nella barra di stato.
	 * @param message il messaggio da mostrare
	 */
	public void setStatus(String message) {
		statusLabel.setText(message);
	}
	
	/**
	 * Restituisce il componente a schede della finestra.
	 * @return Il pannello a schede della finestra.
	 */
	public JTabbedPane getTabbedView() {
		return tabbedView;
	}
	
	/**
	 * Mostra la finestra grafica, dopo averne opportunamente 
	 * disposto tutte le componenti.
	 */
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
	
	/**
	 * Crea la barra di stato, con a sinistra un messaggio descrittivo 
	 * e a destra l'informazione dell'utente connesso.
	 */
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
