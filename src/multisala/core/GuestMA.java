package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

/**
 * L'agente mobile per il client che non ha effettuato il login.
 * @author Davide Mazzoni
 * @author Giacomo Annaloro
 *
 */
public class GuestMA extends AbstractAgent implements IGuest {
	
	public GuestMA(IAuthServer authServer, ICentralServer centralServer) {
		super(authServer, centralServer);
	}
	
	/**
	 * Mostra la programmazione (lista di spettacoli) per un giorno specificato.
	 * @param dt la data del calendario di cui si vuole visualizzare la programmazione
	 * @return La lista di spettacoli in programma per quella data.
	 * @see Show
	 */
	@Override
	public List<Show> getSchedule(Calendar dt) {
		int month = dt.get(Calendar.MONTH) + 1;
		String monthString = new String((month < 10 ? "0" : "") + month);
		int day = dt.get(Calendar.DATE);
		String dayString = new String((day < 10 ? "0" : "") + day);
		String dtString = new String(dt.get(Calendar.YEAR) + "-" + monthString + "-" +  dayString);		
		List<Show> shows = new Vector<Show>();
		try {
			shows = centralServer.getSchedule(dtString);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);	
		}
		return shows;
	}

	/**
	 * Effettua il login invocando l'apposito metodo del server di autenticazione.
	 * @param user il nome utente
	 * @param password la password
	 * @return L'agente mobile per il corrispondente tipo di utente.
	 * @exception LoginException in caso di dati di login non validi.
	 */
	@Override
	public AbstractAgent login(String user, String password) throws LoginException {
		AbstractAgent agent = null;
		try {
			agent = authServer.login(user, password);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
		return agent;
	}

	/**
	 * Effettua la richiesta di registrazione di un nuovo utente, invocando
	 * l'apposito metodo del server centrale.
	 * @param user il nome utente
	 * @param password la password
	 */
	@Override
	public void register(String user, String password) throws AccountException {
		try {
			centralServer.register(user, password);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}
	
}
