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
	public List<Show> getSchedule(String dt) throws RemoteException, SQLException {
		PreparedStatement query = null;
		List<Show> schedule = new Vector<Show>();
		try {
			query = dbConnection.prepareStatement("SELECT * FROM shows WHERE show_date = ?");
			query.setString(1, dt);
			ResultSet rs = query.executeQuery();
			//TODO Costruzione List<Show>
			return schedule;
		} finally {
			query.close();
		}
	}

	@Override
	public void register(String user, String password) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Reservation> getReservations(String user)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertReservation(Reservation res) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void editReservation(int id, Reservation current)
			throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteReservation(int id) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertShow(Show sh) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void editShow(int id, Show current) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteShow(int id) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sellTickets(Show sh, int tickets) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Reservation> getReservations() throws RemoteException {
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
		
	}
}
