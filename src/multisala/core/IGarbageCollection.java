package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia remota per la DGC del server di bootstrap.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface IGarbageCollection extends Remote {

		/**
		 * Invocato dal client quando entra in possesso di una referenza remota.
		 * @return L'identificativo assegnato al client.
		 */
		public Integer gotReference() throws RemoteException;
		
		/**
		 * Invocato dal client che non utilizza più la referenza remota.
		 * @param clientID l'identificativo del client
		 */
		public void releaseReference(Integer clientID) throws RemoteException;
}
