package multisala.core;

import java.util.List;

import multisala.exceptions.ReservationException;

public interface IUser extends IGuest {

	String getUsername();
	List<Reservation> getReservations();
	void insertReservation(Reservation res) throws ReservationException;
	void editReservation(Reservation updated) throws ReservationException;
	void deleteReservation(int id);
	IGuest logout();
	
}
