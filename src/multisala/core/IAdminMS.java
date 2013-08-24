package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAdminMS extends Remote {
	
	List<String> confirmUsers(List<String> users) throws RemoteException;
	void showSoldOut(Show sh) throws RemoteException;

}
