package multisala.core;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBootstrapServer extends Remote {

	MarshalledObject<Runnable> getClient() throws RemoteException;
	
}
