package multisala.core;

import java.util.List;

import multisala.exceptions.ReservationException;

public interface IUser extends IGuest {

	List<Reservation> getReservations(String user);
	void insertReservation(Reservation res) throws ReservationException;
	void editReservation(Reservation updated) throws ReservationException;
	void deleteReservation(int id);
	IGuest logout();
	
}
