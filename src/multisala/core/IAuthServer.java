package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

/**
 * Interfaccia remota del server di autenticazione.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface IAuthServer extends Remote {

	/**
	 * Verifica le credenziali passate e restituisce l'agente mobile
	 * appropriato in base ai permessi dell'utente che ha effettuato
	 * il login.
	 * @param user il nome utente
	 * @param password la password
	 * @return L'agente mobile per l'utente che ha effettuato il login.
	 * @throws LoginException se l'utente non esiste, non ha ricevuto l'approvazione
	 * o in caso di password errata.
	 */
	AbstractAgent login(String user, String password) throws LoginException, RemoteException, SQLException;
	
	/**
	 * Ritorna un agente mobile di tipo {@link IGuest} per il client non registrato.
	 * @return L'agente mobile per il client ospite.
	 */
	IGuest login() throws RemoteException;
	
}
