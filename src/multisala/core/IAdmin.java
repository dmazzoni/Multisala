package multisala.core;

import java.util.List;

public interface IAdmin extends IUser {

	void insertShow(Show sh);
	void editShow(Show updated);
	void deleteShow(int id);
	void sellTickets(Show sh, int tickets);
	List<Reservation> getReservations();
	
}
