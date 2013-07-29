package multisala.core;

import java.util.Calendar;
import java.util.List;

public interface IGuest {
	
	List<Show> getSchedule(Calendar dt);
	void register(String user, String password);

}
