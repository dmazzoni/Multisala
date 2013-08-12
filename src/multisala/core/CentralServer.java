package multisala.core;

import java.io.IOException;
import java.net.Socket;
import java.rmi.MarshalledObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.Unreferenced;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class CentralServer extends Activatable implements ICentralServer, Unreferenced {

	private Connection dbConnection;
	
	public CentralServer(ActivationID id, MarshalledObject<?> obj) 
			throws ClassNotFoundException, IOException, RemoteException, SQLException {
		super(id, 12001, new ClientSocketFactory(30000), null);
		Class.forName("org.sqlite.JDBC");
		dbConnection = DriverManager.getConnection("jdbc:sqlite:multisala.db");
		LocateRegistry.getRegistry(1098).rebind("CentralServer", this);
	}
	
	@Override
	public synchronized List<Show> getSchedule(String dt) throws RemoteException, SQLException {
		PreparedStatement query = null;
		List<Show> schedule = new Vector<Show>();
		try {
			query = dbConnection.prepareStatement("SELECT * FROM shows WHERE show_date = ?");
			query.setString(1, dt);
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("show_id");
				String title = rs.getString("title");
				int theater = rs.getInt("theater_id");
				String date = rs.getString("show_date");
				String time = rs.getString("show_time");
				int seats = rs.getInt("free_seats");
				schedule.add(new Show(id, title, date, time, theater, seats));
			}
			return schedule;
		} finally {
			query.close();
		}
	}

	@Override
	public synchronized void register(String user, String password) throws RemoteException, SQLException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("INSERT OR ROLLBACK INTO users " +
					"VALUES (?, ?, 'user', 0)");
			query.setString(1, user);
			query.setString(2, password);
			query.executeUpdate();
		} finally {
			query.close();
		}
	}

	@Override
	public synchronized List<Reservation> getReservations(String user) throws RemoteException, SQLException {
		PreparedStatement query = null;
		List<Reservation> reservations = new Vector<Reservation>();
		try {
			query = dbConnection.prepareStatement("SELECT a.reservation_id, a.seats," +
					"b.show_id, b.title, b.show_date, b.show_time, b.theater_id, b.free_seats " +
					"FROM reservations AS a, shows AS b " +
					"WHERE a.user_id = ? AND a.show_id = b.show_id");
			query.setString(1, user);
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("b.show_id");
				String title = rs.getString("b.title");
				int theater = rs.getInt("b.theater_id");
				String date = rs.getString("b.show_date");
				String time = rs.getString("b.show_time");
				int seats = rs.getInt("b.free_seats");
				Show s = new Show(id, title, date, time, theater, seats);
				id = rs.getInt("a.reservation_id");
				seats = rs.getInt("a.seats");
				reservations.add(new Reservation(id, s, user, seats));
			}
			return reservations;
		} finally {
			query.close();
		}
	}

	@Override
	public synchronized void insertReservation(Reservation res) throws RemoteException, SQLException {
		PreparedStatement query1 = null;
		PreparedStatement query2 = null;
		try {
			dbConnection.setAutoCommit(false);
			query1 = dbConnection.prepareStatement("INSERT INTO reservations " +
					"VALUES (NULL, ?, ?, ?)");
			query1.setString(1, res.getUser());
			query1.setInt(2, res.getShow().getId());
			query1.setInt(3, res.getSeats());
			query1.executeUpdate();
			query2 = dbConnection.preparedStatement("UPDATE shows SET show_id ///////)
			
		} finally {
			dbConnection.setAutoCommit(true);
			query1.close();
		}
	}

	@Override
	public synchronized void editReservation(Reservation updated) throws RemoteException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("UPDATE OR ROLLBACK reservations " +
					"SET /////////");
			query.setString(1, res.getUser());
			query.setInt(2, res.getShow().getId());
			query.setInt(3, res.getSeats());
			query.executeUpdate();
		} finally {
			query.close();
		}

	}

	@Override
	public synchronized void deleteReservation(int id) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void insertShow(Show sh) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void editShow(int id, Show current) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void deleteShow(int id) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void sellTickets(Show sh, int tickets) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized List<Reservation> getReservations() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void unreferenced() {
		try {
			LocateRegistry.getRegistry(1098).unbind("CentralServer");
			inactive(getID());
			dbConnection.close();
		} catch (RemoteException | NotBoundException | ActivationException | SQLException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	private static class ClientSocketFactory implements RMIClientSocketFactory {

		int timeout;
		
		public ClientSocketFactory(int timeout) {
			this.timeout = timeout;
		}

		@Override
		public Socket createSocket(String host, int port) throws IOException {
			Socket s = new Socket(host, port);
			s.setSoTimeout(timeout);
			return s;
		}
		
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof ClientSocketFactory && this.timeout == ((ClientSocketFactory) obj).timeout);
		}
		
		@Override
		public int hashCode() {
			return timeout;
		}
		
	}
}
