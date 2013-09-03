package multisala.core;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.Unreferenced;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.security.auth.login.LoginException;

/**
 * Implementazione del server di autenticazione attivabile.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public class AuthServer extends Activatable implements IAuthServer, Unreferenced {

	/**
	 * La referenza al server centrale.
	 */
	private ICentralServer centralServer;
	
	/**
	 * La connessione al database.
	 */
	private Connection dbConnection;

	public AuthServer(ActivationID id, MarshalledObject<ICentralServer> centralServer) 
			throws ClassNotFoundException, IOException, RemoteException, SQLException {
		super(id, 12000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		this.centralServer = centralServer.get();
		String dbPath = System.getProperty("multisala.dbLocation");
		Class.forName("org.sqlite.JDBC");
		dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbPath + "multisala.db");
		LocateRegistry.getRegistry(1098).rebind("AuthServer", this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractAgent login(String user, String password) 
			throws LoginException, RemoteException, SQLException {
		PreparedStatement query = null;
		try {
			query = dbConnection.prepareStatement("select password, type from users where user_id = ? and approved = 1");
			query.setString(1, user);
			ResultSet rs = query.executeQuery();
			if (!rs.next())
				throw new LoginException("Utente inesistente o non ancora approvato");
			String rsPassword = rs.getString("password");
			String rsType = rs.getString("type");
			if(!rsPassword.equals(password))
				throw new LoginException("Password errata");
			if(rsType.equals("user"))
				return new UserMA(this, centralServer, user);
			else
				return new AdminMS(this, centralServer, user);
		} finally {
			query.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGuest login() throws RemoteException {
		return new GuestMA(this, centralServer);
	}

	/**
	 * Deregistra il server dal servizio di naming e lo disattiva,
	 * rendendolo disponibile per la GC locale.
	 */
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
