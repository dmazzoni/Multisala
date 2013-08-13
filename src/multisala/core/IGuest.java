package multisala.core;

import java.util.Calendar;
import java.util.List;

import javax.security.auth.login.LoginException;

public interface IGuest {
	
	List<Show> getSchedule(Calendar dt);
	GenericClient login(String user, String password) throws LoginException;
	void register(String user, String password);

}
