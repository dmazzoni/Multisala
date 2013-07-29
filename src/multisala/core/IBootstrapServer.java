package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBootstrapServer extends Remote {

	GuestUI getClient() throws RemoteException;
	
}
