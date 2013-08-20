package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import multisala.gui.AbstractUI;

public class GuestMA extends AbstractAgent implements IGuest {
	
	public GuestMA(IAuthServer authServer, ICentralServer centralServer) {
		super(authServer, centralServer);
	}
	
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

	@Override
	public void register(String user, String password) throws AccountException {
		try {
			centralServer.register(user, password);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
		}
	}
	
}
