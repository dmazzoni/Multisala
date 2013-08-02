package multisala.core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.rmi.PortableRemoteObject;

public class BootstrapServer implements IBootstrapServer {

	public BootstrapServer() throws RemoteException {
		UnicastRemoteObject.exportObject(this);
		PortableRemoteObject.exportObject(this);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public GuestUI getClient() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
