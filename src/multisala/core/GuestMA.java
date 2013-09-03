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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	@Override
	public void register(String user, String password) throws AccountException {
		try {
			centralServer.register(user, password);
			window.setStatus("Richiesta di registrazione effettuata con successo");
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}
	
}
