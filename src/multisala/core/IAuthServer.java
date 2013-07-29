package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthServer extends Remote {

	GenericClient login(String user, String password) throws RemoteException;
	
}
