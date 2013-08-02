package multisala.core;

import java.io.*;
import java.rmi.MarshalledObject;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.activation.*;
import java.rmi.registry.LocateRegistry;
import java.sql.*;
import java.util.Properties;

public final class Setup {

	public static void main(String[] args) {
		String groupPolicy = System.getProperty("multisala.policy");
		String implCodebase = System.getProperty("multisala.codebase");
		String path = System.getProperty("multisala.refLocation");
		// LANCIO IL SECURITY MANAGER
		System.setSecurityManager(new RMISecurityManager());
		try {
			Properties groupProperties = new Properties();
			groupProperties.put("java.security.policy", groupPolicy);
			groupProperties.put("multisala.codebase", implCodebase);
			groupProperties.put("java.class.path", "no_classpath");
			
			/** Controllo se esistono i file dei GroupID.
			Se esistono, estraggo i GroupID e passo a fase 3
			Se non esiste, creo, registro e salvo su file, poi fase 3
		**/
			// Registrazione dei gruppi o recupero del loro ID
			ActivationGroupID groupID1 = getGroupID("group1", groupProperties);
			ActivationGroupID groupID2 = getGroupID("group2", groupProperties);
			// Creazione dei descrittori e registrazione presso i gruppi
			ActivationDesc centralDesc = new ActivationDesc(groupID2, "multisala.core.CentralServer", implCodebase, null);
			ICentralServer centralStub = (ICentralServer) Activatable.register(centralDesc);
			ActivationDesc authDesc = new ActivationDesc(groupID1, "multisala.core.AuthServer", implCodebase, new MarshalledObject<ICentralServer>(centralStub));
			IAuthServer authStub = (IAuthServer) Activatable.register(authDesc);
			// Binding del server di autenticazione
			LocateRegistry.getRegistry(1098).bind("AuthServer", authStub);
			// Backup su disco dello stub al server di autenticazione
			saveObject(path + "AuthServer", authStub);
			initDB();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveObject(String path, Object obj) {
		try {
			FileOutputStream fStream = new FileOutputStream(path);
			ObjectOutputStream oStream = new ObjectOutputStream(fStream);
			oStream.writeObject(obj);
			oStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static ActivationGroupID getGroupID(String path, Properties groupProperties) 
			throws ActivationException, IOException, ClassNotFoundException {
		ActivationGroupID groupID;
		File f = new File(path);
		
		if (f.exists()) {
			FileInputStream fStream = new FileInputStream(f);
			ObjectInputStream oStream = new ObjectInputStream(fStream);
			groupID = (ActivationGroupID) oStream.readObject();
			oStream.close();
			return groupID;
		}
		ActivationGroupDesc groupDesc = new ActivationGroupDesc(groupProperties, null);
		groupID = ActivationGroup.getSystem().registerGroup(groupDesc);
		saveObject(path, groupID);
		return groupID;
	}
	
	private static void initDB()
			throws ClassNotFoundException, SQLException {
		Connection dbConnection;
		
		Class.forName("org.sqlite.JDBC");
		dbConnection = DriverManager.getConnection("jdbc:sqlite:multisala.db");
		Statement dbStatement = dbConnection.createStatement();
		dbStatement.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
										"user_id INTEGER PRIMARY KEY" +
										"name VARCHAR(30) NOT NULL," +
										"password VARCHAR(15) NOT NULL," +
										"type VARCHAR(10) NOT NULL," +
										"approved BOOLEAN NOT NULL)");
		dbStatement.executeUpdate("CREATE TABLE IF NOT EXISTS shows (" +
										"show_id INTEGER PRIMARY KEY," +
										"title VARCHAR(30) NOT NULL," +
										"time DATETIME NOT NULL," +
										"theater VARCHAR(15) NOT NULL)");
		dbStatement.executeUpdate("CREATE TABLE IF NOT EXISTS reservations (" +
										"reservation_id INTEGER PRIMARY KEY," +
										"user_id INTEGER NOT NULL," +
										"show_id INTEGER NOT NULL," +
										"seats INTEGER NOT NULL" +
										"FOREIGN KEY(user_id) REFERENCES users(user_id)" +
										"FOREIGN KEY(show_id) REFERENCES shows(show_id)");
		dbConnection.close();
	}
}
