package multisala.core;

import java.io.Serializable;

import multisala.gui.AbstractUI;

public abstract class AbstractAgent implements Serializable {

	protected IAuthServer authServer;
	protected ICentralServer centralServer;
	
	protected AbstractUI window;

	protected AbstractAgent(IAuthServer authServer, ICentralServer centralServer) {
		this.authServer = authServer;
		this.centralServer = centralServer;
	}

	public void setWindow(AbstractUI window) {
		this.window = window;
	}
	
}