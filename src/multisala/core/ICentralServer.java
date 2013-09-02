package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.AccountException;

import multisala.exceptions.ReservationException;

public interface ICentralServer extends Remote {

	List<Show> getSchedule(String dt) throws RemoteException, SQLException;
	void register(String user, String password) throws AccountException, RemoteException, SQLException;
	
	List<Reservation> getReservations() throws RemoteException, SQLException;
	List<Reservation> getReservations(String user) throws RemoteException, SQLException;
	void insertReservation(Reservation res) throws RemoteException, ReservationException, SQLException;
	void editReservation(Reservation current, Reservation updated) throws RemoteException, ReservationException, SQLException;
	void deleteReservation(int id) throws RemoteException, SQLException;
	
	void insertShow(Show sh) throws RemoteException, SQLException;
	void editShow(Show updated) throws RemoteException, SQLException;
	void deleteShow(int id) throws RemoteException, SQLException;
	void sellTickets(Show sh, int tickets) throws RemoteException, SQLException;
	
	void adminConnected(IAdminMS admin) throws RemoteException;
	void adminDisconnected(IAdminMS admin) throws RemoteException;
	
}
