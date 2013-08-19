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

	protected AbstractUI window;
	
	public GuestMA(IAuthServer authServer, ICentralServer centralServer) {
		super(authServer, centralServer);
	}
	
	@Override
	public List<Show> getSchedule(Calendar dt) {
		String dtString = new String(dt.get(Calendar.YEAR) + "-" + (dt.get(Calendar.MONTH) + 1) + "-" +  dt.get(Calendar.DATE));		
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

	public void setWindow(AbstractUI window) {
		this.window = window;
	}
}
