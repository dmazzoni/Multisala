package multisala.core;

import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

public interface IGuest {
	
	List<Show> getSchedule(Date dt);
	AbstractAgent login(String user, String password) throws LoginException;
	void register(String user, String password);

}
