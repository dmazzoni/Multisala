package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;

public interface ICentralServer extends Remote {

	List<Show> getSchedule(Calendar dt) throws RemoteException;
	void register(String user, String password) throws RemoteException;
	
	List<Reservation> getReservations(String user) throws RemoteException;
	void insertReservation(Reservation res) throws RemoteException;
	void editReservation(int id, Reservation current) throws RemoteException;
	void deleteReservation(int id) throws RemoteException;
	
	void insertShow(Show sh) throws RemoteException;
	void editShow(int id, Show current) throws RemoteException;
	void deleteShow(int id) throws RemoteException;
	void sellTickets(Show sh, int tickets) throws RemoteException;
	List<Reservation> getReservations() throws RemoteException;
	
}
