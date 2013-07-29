package multisala.core;

public abstract class GenericClient {

	ICentralServer centralServer;

	protected GenericClient(ICentralServer centralServer) {
		this.centralServer = centralServer;
	}

}
