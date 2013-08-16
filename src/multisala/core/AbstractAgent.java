package multisala.core;

import java.io.Serializable;

public abstract class AbstractAgent implements Serializable {

	protected IAuthServer authServer;
	protected ICentralServer centralServer;

	protected AbstractAgent(IAuthServer authServer, ICentralServer centralServer) {
		this.authServer = authServer;
		this.centralServer = centralServer;
	}

}
