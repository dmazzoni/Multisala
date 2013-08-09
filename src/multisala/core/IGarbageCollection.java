package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGarbageCollection extends Remote {

		public Integer gotReference() throws RemoteException;
		public void releaseReference(Integer clientID) throws RemoteException;
}
