package multisala.core;

import java.util.Calendar;
import java.util.List;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;

import multisala.gui.AbstractUI;

/**
 * Interfaccia dell'agente mobile del client che
 * non ha effettuato il login.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface IGuest {
	
	/**
	 * Mostra la programmazione (lista di spettacoli) per un giorno specificato.
	 * @param dt la data del calendario di cui si vuole visualizzare la programmazione
	 * @return La lista di spettacoli in programma per quella data.
	 * @see Show
	 */
	List<Show> getSchedule(Calendar dt);
	
	/**
	 * Effettua il login invocando l'apposito metodo del server di autenticazione.
	 * @param user il nome utente
	 * @param password la password
	 * @return L'agente mobile per il corrispondente tipo di utente.
	 * @exception LoginException in caso di dati di login non validi.
	 */
	AbstractAgent login(String user, String password) throws LoginException;
	
	/**
	 * Effettua la richiesta di registrazione di un nuovo utente, invocando
	 * l'apposito metodo del server centrale.
	 * @param user il nome utente
	 * @param password la password
	 */
	void register(String user, String password) throws AccountException;

	/**
	 * @see AbstractAgent#setWindow(AbstractUI)
	 */
	void setWindow(AbstractUI guestUI);

}
