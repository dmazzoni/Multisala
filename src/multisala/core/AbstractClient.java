package multisala.core;

import java.io.Serializable;

public abstract class AbstractClient implements Serializable {

	ICentralServer centralServer;

	protected AbstractClient(ICentralServer centralServer) {
		this.centralServer = centralServer;
	}

}
