package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGarbageCollection extends Remote {

		public ClientLease gotReference() throws RemoteException;
		public ClientLease refreshReference(ClientLease lease) throws RemoteException;
		public void releaseReference(ClientLease lease) throws RemoteException;
}
