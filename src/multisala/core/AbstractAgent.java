package multisala.core;

import java.io.Serializable;

import multisala.gui.AbstractUI;

/**
 * Generico agente mobile che implementa le funzionalità comuni 
 * tra i diversi agenti inviati ai client.
 *  @author Davide Mazzoni
 *  @author Giacomo Annaloro
 */
public abstract class AbstractAgent implements Serializable {

	/**
	 * La referenza al server di autenticazione.
	 */
	protected IAuthServer authServer;
	
	/**
	 * La referenza al server centrale.
	 */
	protected ICentralServer centralServer;
	
	/**
	 * La finestra grafica.
	 */
	protected AbstractUI window;

	protected AbstractAgent(IAuthServer authServer, ICentralServer centralServer) {
		this.authServer = authServer;
		this.centralServer = centralServer;
	}

	/**
	 * Setta la finestra grafica, che cambia a seconda del tipo di agente mobile
	 * @param window la finestra grafica
	 */
	public void setWindow(AbstractUI window) {
		this.window = window;
	}
	
}