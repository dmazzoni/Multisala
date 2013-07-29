package multisala.core;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationID;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class AuthServer extends Activatable implements IAuthServer {

	private ICentralServer centralServer;

	public AuthServer(ActivationID id, MarshalledObject<ICentralServer> centralServer) 
			throws ClassNotFoundException, IOException, RemoteException {
		super(id, 12000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		this.centralServer = (ICentralServer) centralServer.get();
	}
	
	@Override
	public GenericClient login(String user, String password)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
