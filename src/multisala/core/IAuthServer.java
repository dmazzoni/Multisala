package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

public interface IAuthServer extends Remote {

	GenericClient login(String user, String password) throws LoginException, RemoteException, SQLException;
	IGuest login() throws RemoteException;
	
}
