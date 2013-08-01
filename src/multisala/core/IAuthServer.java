package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IAuthServer extends Remote {

	GenericClient login(String user, String password) throws RemoteException, SQLException;
	IGuest login() throws RemoteException;
	
}
