package multisala.core;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import multisala.gui.AbstractUI;

public class GuestMA extends AbstractAgent implements IGuest {

	protected AbstractUI window;
	
	public GuestMA(IAuthServer authServer, ICentralServer centralServer) {
		super(authServer, centralServer);
	}
	
	@Override
	public List<Show> getSchedule(Date dt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractAgent login(String user, String password) throws LoginException {
		try {
			return authServer.login(user, password);
		} catch (RemoteException | SQLException e) {
			JOptionPane.showMessageDialog(window, e);
			return null;
		}
	}

	@Override
	public void register(String user, String password) {
		// TODO Auto-generated method stub

	}

}
