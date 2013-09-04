package multisala.core;

import java.io.*;
import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;
import java.rmi.activation.*;
import java.rmi.registry.LocateRegistry;
import java.sql.*;
import java.util.Properties;

/**
 * Codice di setup del sistema di attivazione.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 */
public final class Setup {

	/**
	 * Effettua il setup dei gruppi di attivazione e dei relativi server
	 * attivabili, registrando il server di autenticazione sul registro
	 * RMI lanciato da RMID. <br>Inizializza inoltre il database 
	 * utilizzato dai server.
	 */
	public static void main(String[] args) {
		String groupPolicy = System.getProperty("multisala.policy");
		String implCodebase = System.getProperty("multisala.codebase");
		String certPath = System.getProperty("multisala.certLocation");
		String dbPath = System.getProperty("multisala.dbLocation");
		String hostname = System.getProperty("java.rmi.server.hostname");
		System.setSecurityManager(new RMISecurityManager());
		try {
			Properties group2Properties = new Properties();
			group2Properties.put("java.security.policy", groupPolicy);
			group2Properties.put("multisala.codebase", implCodebase);
			group2Properties.put("java.class.path", "no_classpath");
			group2Properties.put("multisala.dbLocation", dbPath);
			group2Properties.put("java.rmi.server.hostname", hostname);
			
			Properties group1Properties = new Properties();
			group1Properties.put("javax.net.ssl.debug", "all");
			group1Properties.put("javax.net.ssl.keyStore", certPath + "serverKeys");
			group1Properties.put("javax.net.ssl.keyStorePassword", "multisala");
			group1Properties.put("javax.net.ssl.trustStore", certPath + "serverTrust");
			group1Properties.put("javax.net.ssl.trustStorePassword", "multisala");
			group1Properties.put("java.security.policy", groupPolicy);
			group1Properties.put("multisala.codebase", implCodebase);
			group1Properties.put("java.class.path", "no_classpath");
			group1Properties.put("multisala.dbLocation", dbPath);
			group1Properties.put("java.rmi.server.hostname", hostname);

			// Registrazione dei gruppi o recupero del loro ID
			ActivationGroupID groupID1 = getGroupID("group1", group1Properties);
			ActivationGroupID groupID2 = getGroupID("group2", group2Properties);
			// Creazione dei descrittori e registrazione presso i gruppi
			ActivationDesc centralDesc = new ActivationDesc(groupID2, "multisala.core.CentralServer", implCodebase, null);
			ICentralServer centralStub = (ICentralServer) Activatable.register(centralDesc);
			ActivationDesc authDesc = new ActivationDesc(groupID1, "multisala.core.AuthServer", implCodebase, new MarshalledObject<ICentralServer>(centralStub));
			IAuthServer authStub = (IAuthServer) Activatable.register(authDesc);
			// Binding del server di autenticazione
			LocateRegistry.getRegistry(1098).rebind("AuthServer", authStub);
			initDB();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restituisce l'ID del gruppo di attivazione richiesto. La referenza viene
	 * recuperata da file in caso il gruppo sia stato precedentemente registrato,
	 * altrimenti viene ottenuta effettuandone la registrazione presso RMID.
	 * @param path il percorso in cui cercare la referenza al gruppo
	 * @param groupProperties le proprietà con cui creare il descrittore del gruppo
	 * @return L'ID del gruppo di attivazione.
	 */
	private static ActivationGroupID getGroupID(String path, Properties groupProperties) 
			throws ActivationException, IOException, ClassNotFoundException {
		ActivationGroupID groupID;
		File f = new File(path);
		
		if (f.exists()) {
			FileInputStream fInStream = new FileInputStream(f);
			ObjectInputStream oInStream = new ObjectInputStream(fInStream);
			groupID = (ActivationGroupID) oInStream.readObject();
			oInStream.close();
			return groupID;
		}
		ActivationGroupDesc groupDesc = new ActivationGroupDesc(groupProperties, null);
		groupID = ActivationGroup.getSystem().registerGroup(groupDesc);
		
		FileOutputStream fOutStream = new FileOutputStream(path);
		ObjectOutputStream oOutStream = new ObjectOutputStream(fOutStream);
		oOutStream.writeObject(groupID);
		oOutStream.close();
		
		return groupID;
	}
	
	/**
	 * Inizializza il database SQLite utilizzato dal server centrale 
	 * e dal server di autenticazione.
	 */
	private static void initDB()
			throws ClassNotFoundException, SQLException {
		Connection dbConnection;
		
		Class.forName("org.sqlite.JDBC");
		dbConnection = DriverManager.getConnection("jdbc:sqlite:multisala.db");
		Statement dbStatement = dbConnection.createStatement();
		dbStatement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
										"user_id VARCHAR(30) PRIMARY KEY," +
										"password VARCHAR(15) NOT NULL," +
										"type VARCHAR(10) NOT NULL," +
										"approved BOOLEAN NOT NULL)");
		dbStatement.executeUpdate("CREATE TABLE IF NOT EXISTS shows (" +
										"show_id INTEGER PRIMARY KEY," +
										"title VARCHAR(30) NOT NULL," +
										"show_date CHARACTER(10) NOT NULL," +
										"show_time CHARACTER(5) NOT NULL," +
										"theater VARCHAR(5) NOT NULL," +
										"free_seats INTEGER NOT NULL CHECK (free_seats >= 0) DEFAULT 150)");
		dbStatement.executeUpdate("CREATE TABLE IF NOT EXISTS reservations (" +
										"reservation_id INTEGER PRIMARY KEY," +
										"user_id VARCHAR(30) NOT NULL," +
										"show_id INTEGER NOT NULL," +
										"seats INTEGER NOT NULL CHECK (seats >= 0)," +
										"FOREIGN KEY(user_id) REFERENCES users(user_id)," +
										"FOREIGN KEY(show_id) REFERENCES shows(show_id))");
		dbStatement.executeUpdate("INSERT OR IGNORE INTO shows values (0, 'Skyfall', '2013-09-02', '21:15', '3', 150)");
		dbStatement.executeUpdate("INSERT OR IGNORE INTO users values ('admin', 'admin', 'admin', 1)");
		dbStatement.executeUpdate("INSERT OR IGNORE INTO users values ('james', 'cameron', 'user', 1)");
		dbConnection.close();
	}
}
