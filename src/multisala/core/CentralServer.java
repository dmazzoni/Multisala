package multisala.core;

import java.io.IOException;
import java.io.Serializable;
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
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class CentralServer extends Activatable implements ICentralServer, Unreferenced {

	private Connection dbConnection;
	private Set<IAdminMS> administrators;
	private List<String> pendingUsers;
	
	public CentralServer(ActivationID id, MarshalledObject<?> obj) 
			throws ClassNotFoundException, IOException, RemoteException, SQLException {
		super(id, 12001, new ClientSocketFactory(30000), null);
		Class.forName("org.sqlite.JDBC");
		dbConnection = DriverManager.getConnection("jdbc:sqlite:multisala.db");
		administrators = new HashSet<IAdminMS>();
		pendingUsers = new Vector<String>();
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
				String theater = rs.getString("theater");
				String date = rs.getString("show_date");
				String time = rs.getString("show_time");
				int seats = rs.getInt("free_seats");
				schedule.add(new Show(id, title, date, time, theater, seats));
			}
			return schedule;
		} finally {
			if (query != null)
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
			if (query != null)
				query.close();
		}
	}
	
	@Override
	public synchronized List<Reservation> getReservations() throws RemoteException, SQLException {
		Statement query = null;
		List<Reservation> reservations = new Vector<Reservation>();
		try {
			query = dbConnection.createStatement();
			ResultSet rs = query.executeQuery("SELECT user_id FROM users");
			while (rs.next())
				reservations.addAll(getReservations(rs.getString("user_id")));
			return reservations;
		} finally {
			if (query != null)
				query.close();
		}
	}

	@Override
	public synchronized List<Reservation> getReservations(String user) throws RemoteException, SQLException {
		PreparedStatement query = null;
		List<Reservation> reservations = new Vector<Reservation>();
		try {
			query = dbConnection.prepareStatement("SELECT a.reservation_id, a.seats," +
					"b.show_id, b.title, b.show_date, b.show_time, b.theater, b.free_seats " +
					"FROM reservations AS a, shows AS b " +
					"WHERE a.user_id = ? AND a.show_id = b.show_id");
			query.setString(1, user);
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("show_id");
				String title = rs.getString("title");
				String date = rs.getString("show_date");
				String time = rs.getString("show_time");
				String theater = rs.getString("theater");
				int seats = rs.getInt("free_seats");
				Show s = new Show(id, title, date, time, theater, seats);
				id = rs.getInt("reservation_id");
				seats = rs.getInt("seats");
				reservations.add(new Reservation(id, s, user, seats));
			}
			return reservations;
		} finally {
			if (query != null)
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
			query2 = dbConnection.prepareStatement("UPDATE shows SET free_seats = free_seats - ?" +
					"WHERE show_id = ?");
			query2.setInt(1, res.getSeats());
			query2.setInt(2, res.getShow().getId());
			query2.executeUpdate();
			dbConnection.commit();
			checkFreeSeats(res.getShow());
		} catch (SQLException e) {
			dbConnection.rollback();
			throw e;
		} finally {
			dbConnection.setAutoCommit(true);
			if (query1 != null)
				query1.close();
			if (query2 != null)
				query2.close();
		}
	}

	@Override
	public synchronized void editReservation(Reservation updated) throws RemoteException, SQLException {
		PreparedStatement query1 = null;
		PreparedStatement query2 = null;
		try {
			dbConnection.setAutoCommit(false);
			query1 = dbConnection.prepareStatement("UPDATE shows SET free_seats = free_seats + " +
					"((SELECT seats FROM reservations WHERE reservation_id = ?) - ?)");
			query1.setInt(1, updated.getId());
			query1.setInt(2, updated.getSeats());
			query1.executeUpdate();
			query2 = dbConnection.prepareStatement("UPDATE reservations SET seats = ? WHERE reservation_id = ?");
			query2.setInt(1, updated.getSeats());
			query2.setInt(2, updated.getId());
			query2.executeUpdate();
			dbConnection.commit();
			checkFreeSeats(updated.getShow());
		} catch (SQLException e) { 
			dbConnection.rollback();
			throw e;
		} finally {
			dbConnection.setAutoCommit(true);
			if (query1 != null)
				query1.close();
			if (query2 != null)
				query2.close();
		}
	}

	@Override
	public synchronized void deleteReservation(int id) throws RemoteException, SQLException {
		PreparedStatement query1 = null;
		PreparedStatement query2 = null;
		try {
			dbConnection.setAutoCommit(false);
			query1 = dbConnection.prepareStatement("UPDATE shows SET free_seats = free_seats + " +
					"(SELECT seats FROM reservations WHERE reservation_id = ?)");
			query1.setInt(1, id);
			query1.executeUpdate();
			query2 = dbConnection.prepareStatement("DELETE FROM reservations WHERE reservation_id = ?");
			query2.setInt(1, id);
			query2.executeUpdate();
			dbConnection.commit();
		} catch (SQLException e) { 
			dbConnection.rollback();
			throw e;
		} finally {
			dbConnection.setAutoCommit(true);
			if (query1 != null)
				query1.close();
			if (query2 != null)
				query2.close();
		}
	}

	@Override
	public synchronized void insertShow(Show sh) throws RemoteException, SQLException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("INSERT OR ROLLBACK INTO shows " +
					"(title, show_date, show_time, theater) VALUES (?, ?, ?, ?)");
			query.setString(1, sh.getTitle());
			query.setString(2, sh.getDate());
			query.setString(3, sh.getTime());
			query.setString(4, sh.getTheater());
			query.executeUpdate();
		} finally {
			if (query != null)
				query.close();
		}
	}

	@Override
	public synchronized void editShow(Show updated) throws RemoteException, SQLException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("UPDATE OR ROLLBACK shows " +
					"SET title = ?, show_date = ?, show_time = ?, theater = ? " +
					"WHERE show_id = ?");
			query.setString(1, updated.getTitle());
			query.setString(2, updated.getDate());
			query.setString(3, updated.getTime());
			query.setString(4, updated.getTheater());
			query.setInt(5, updated.getId());
			query.executeUpdate();
		} finally {
			if (query != null)
				query.close();
		}
	}

	@Override
	public synchronized void deleteShow(int id) throws RemoteException, SQLException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("DELETE FROM shows WHERE show_id = ?");
			query.setInt(1, id);
			query.executeUpdate();
		} finally {
			if (query != null)
				query.close();
		}
	}

	@Override
	public synchronized void sellTickets(Show sh, int tickets) throws RemoteException, SQLException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("UPDATE OR ROLLBACK shows " +
					"SET free_seats = free_seats - ? WHERE show_id = ?");
			query.setInt(1, tickets);
			query.setInt(2, sh.getId());
			query.executeUpdate();
			checkFreeSeats(sh);
		} finally {
			if (query != null)
				query.close();
		}
	}
	
	@Override
	public synchronized void adminConnected(IAdminMS admin) {
		administrators.add(admin);
		try {
			notifyAdmins();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void adminDisconnected(IAdminMS admin) {
		administrators.remove(admin);
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

	private void notifyAdmins() throws SQLException {
		List<String> confirmedUsers = new Vector<String>();
		for(IAdminMS admin : administrators) {
			try {
				confirmedUsers = admin.confirmUsers(pendingUsers);
				break;
			} catch (RemoteException e) {
				administrators.remove(admin);
			}
		}
		updateUsers(confirmedUsers);
	}
	
	private void updateUsers(List<String> confirmedUsers) throws SQLException {
		PreparedStatement query = dbConnection.prepareStatement("UPDATE OR ROLLBACK users SET approved = 1 " +
				"WHERE user_id = ?");
		try {
			for(String user : confirmedUsers) {
				query.setString(1, user);
				query.executeUpdate();
				pendingUsers.remove(user);
			}
			query = dbConnection.prepareStatement("DELETE FROM users WHERE approved = 0");
			query.executeUpdate();
		} finally {
			if (query != null)
				query.close();
		}
	}
	
	private void checkFreeSeats(Show sh) throws SQLException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("SELECT free_seats FROM shows WHERE show_id = ?");
			query.setInt(1, sh.getId());
			ResultSet rs = query.executeQuery();
			if (rs.next() && rs.getInt("free_seats") == 0)
				for (IAdminMS admin : administrators) {
					try {
						admin.showSoldOut(sh);
						break;
					} catch (RemoteException e) {
						administrators.remove(admin);
					}
				}
		} finally {
			if (query != null)
				query.close();
		}
	}
	
	private static class ClientSocketFactory implements RMIClientSocketFactory, Serializable {

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
