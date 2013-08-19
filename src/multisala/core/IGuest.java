package multisala.core;

import java.util.Calendar;
import java.util.List;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;

import multisala.gui.AbstractUI;

public interface IGuest {
	
	List<Show> getSchedule(Calendar dt);
	AbstractAgent login(String user, String password) throws LoginException;
	void register(String user, String password) throws AccountException;
	void setWindow(AbstractUI window);

}
