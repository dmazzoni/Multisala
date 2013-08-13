package multisala.core;

import java.util.Calendar;
import java.util.List;

public interface IGuest {
	
	List<Show> getSchedule(Calendar dt);
	GenericClient login(String user, String password);
	void register(String user, String password);

}
