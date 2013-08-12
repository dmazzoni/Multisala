package multisala.core;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.MarshalledObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class AuthServer extends Activatable implements IAuthServer, Unreferenced {

	private ICentralServer centralServer;
	private Connection dbConnection;

	public AuthServer(ActivationID id, MarshalledObject<ICentralServer> centralServer) 
			throws ClassNotFoundException, IOException, RemoteException, SQLException {
		super(id, 12000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		this.centralServer = centralServer.get();
		Class.forName("org.sqlite.JDBC");
		dbConnection = DriverManager.getConnection("jdbc:sqlite:multisala.db");
		LocateRegistry.getRegistry(1098).rebind("AuthServer", this);
	}
	
	@Override
	public GenericClient login(String user, String password) 
			throws RemoteException, SQLException {
		PreparedStatement query;
		try {
			query = dbConnection.prepareStatement("select password, type from users where user_id = ? and approved = 1");
			query.setString(1, user);
			ResultSet rs = query.executeQuery();
			if (!rs.first())
				throw new RemoteException("Login fallito.");
			String rsPassword = rs.getString("password");
			String rsType = rs.getString("type");
			if(!rsPassword.equals(password))
				throw new RemoteException("Login fallito.");
			if(rsType.equals("user"))
				return new UserMA(centralServer);
			else
				return new AdminMS(centralServer);
		} finally {
			query.close();
		}
	}
	
	@Override
	public IGuest login() throws RemoteException {
		return new GuestMA(centralServer);
	}

	@Override
	public void unreferenced() {
		try {
			LocateRegistry.getRegistry(1098).unbind("AuthServer");
			inactive(getID());
			dbConnection.close();
		} catch (RemoteException | NotBoundException | ActivationException | SQLException e) {
			e.printStackTrace();
		}
		System.gc();
	}

}
