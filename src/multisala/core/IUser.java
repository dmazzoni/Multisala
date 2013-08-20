package multisala.core;

import java.util.List;

public interface IUser extends IGuest {

	List<Reservation> getReservations(String user);
	void insertReservation(Reservation res);
	void editReservation(Reservation updated);
	void deleteReservation(int id);
	IGuest logout();
	
}
