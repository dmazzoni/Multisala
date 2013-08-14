package multisala.core;

import java.io.Serializable;

public abstract class AbstractAgent implements Serializable {

	ICentralServer centralServer;

	protected AbstractAgent(ICentralServer centralServer) {
		this.centralServer = centralServer;
	}

}
