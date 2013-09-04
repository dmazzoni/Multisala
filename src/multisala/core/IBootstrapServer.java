package multisala.core;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * L'interfaccia remota del server di bootstrap.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface IBootstrapServer extends Remote {

	/**
	 * Ritorna al client minimale l'oggetto <code>Runnable</code>
	 * che permette di lanciare l'applicazione.
	 * @return L'applicazione lato client.
	 */
	MarshalledObject<Runnable> getClient() throws RemoteException;
	
}
