package multisala.core;

import java.io.Serializable;

public abstract class GenericClient implements Serializable {

	ICentralServer centralServer;

	protected GenericClient(ICentralServer centralServer) {
		this.centralServer = centralServer;
	}

}
