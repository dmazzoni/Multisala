package multisala.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import multisala.gui.GuestUI;

/**
 * Implementazione del server di bootstrap, esportato sui protocolli
 * JRMP e IIOP, che supporta la DGC.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public class BootstrapServer implements IBootstrapServer, IGarbageCollection {
	
	/**
	 * L'agente mobile da incapsulare nella finestra grafica
	 * inviata ai client che effettuano il bootstrap.
	 */
	private IGuest mobileAgent;
	
	/**
	 * La referenza al registro RMI.
	 */
	private InitialContext RMIRegistry;
	
	/**
	 * La referenza al COS Naming.
	 */
	private InitialContext COSNaming;
	
	/**
	 * Il conteggio delle referenze remote attive.
	 */
	private Integer refCount = 0;
	
	/**
	 * Il timer che gestisce le scadenze dei lease accordati ai client.
	 */
	private Timer dgcTimer;
	
	/**
	 * Associa agli ID dei client i rispettivi task che saranno lanciati
	 * allo scadere del lease per decrementare il conteggio delle referenze.
	 * @see DGCTask
	 */
	private ConcurrentMap<Integer, DGCTask> dgcTasks;
	
	/**
	 * L'ultimo ID progressivo assegnato ad un client.
	 */
	private static Integer maxClientID = 0;

	/**
	 * Costruisce un server di bootstrap esportandolo sui protocolli JRMP e IIOP.
	 * @param auth la referenza al server di autenticazione
	 * @param cxt1 la referenza al registro RMI
	 * @param cxt2 la referenza al COS Naming
	 */
	public BootstrapServer(IAuthServer auth, InitialContext cxt1, InitialContext cxt2) throws RemoteException {
		mobileAgent = auth.login();
		System.out.println("Guest mobile agent: " + mobileAgent);
		RMIRegistry = cxt1;
		COSNaming = cxt2;
		dgcTimer = new Timer();
		dgcTasks = new ConcurrentHashMap<Integer, DGCTask>();
		UnicastRemoteObject.exportObject(this, 12002);
		PortableRemoteObject.exportObject(this);
		gotReference();
	}
	
	/**
	 * Codice di lancio del server di bootstrap.
	 */
	public static void main(String[] args) {
        try {
        	if (System.getSecurityManager() == null)
        		System.setSecurityManager(new RMISecurityManager());
        	
			Properties prop1 = new Properties();
			prop1.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
			prop1.put(Context.PROVIDER_URL, "rmi://localhost:1098");
			InitialContext cxt1 = new InitialContext(prop1);
			
			Properties prop2 = new Properties();
			prop2.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
			prop2.put("java.naming.provider.url", "iiop://localhost:2098");
			InitialContext cxt2 = new InitialContext(prop2);
			
			String certPath = System.getProperty("multisala.certLocation");
			System.setProperty("javax.net.ssl.debug", "all");
			System.setProperty("javax.net.ssl.keyStore", certPath + "clientKeys");
			System.setProperty("javax.net.ssl.keyStorePassword", "multisala");
			System.setProperty("javax.net.ssl.trustStore", certPath + "clientTrust");
			System.setProperty("javax.net.ssl.trustStorePassword", "multisala");
			
			IAuthServer auth = getAuthRef(cxt1);

			IBootstrapServer bootServer = new BootstrapServer(auth, cxt1, cxt2);
			cxt1.rebind("BootstrapServer", bootServer);
			cxt2.rebind("BootstrapServer", bootServer);
		} catch (ClassNotFoundException | NamingException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc} <br>
	 * Tale oggetto rappresenta una finestra grafica di tipo {@link GuestUI},
	 * che incapsula al suo interno un agente mobile.
	 */
	@Override
	public MarshalledObject<Runnable> getClient() throws RemoteException {
		return wrap(new GuestUI(mobileAgent));
	}
	
	/**
	 * Incapsula l'oggetto ricevuto all'interno di un {@link MarshalledObject}.
	 * @param obj l'oggetto da incapsulare
	 * @return Il <code>MarshalledObject</code> contenente l'oggetto ricevuto.
	 */
	private MarshalledObject<Runnable> wrap(Runnable obj) {
		try {
			return new MarshalledObject<Runnable>(obj);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Restituisce una referenza al server di autenticazione. Tale referenza viene ottenuta
	 * effettuando una lookup sul registro RMI o, in caso di fallimento, leggendola da file.
	 * @param cxt la referenza al registro RMI
	 * @return La referenza al server di autenticazione.
	 * @see IAuthServer
	 */
	private static IAuthServer getAuthRef(InitialContext cxt) throws ClassNotFoundException, IOException, NamingException {
		IAuthServer auth = null;
		File f = new File("authRef");
		try {
			auth = (IAuthServer) cxt.lookup("AuthServer");
			if(!f.exists()) {
				FileOutputStream fOutStream = new FileOutputStream(f);
				ObjectOutputStream oOutStream = new ObjectOutputStream(fOutStream);
				oOutStream.writeObject(auth);
				oOutStream.close();
			}
		} catch (NameNotFoundException e) {
			FileInputStream fInStream = new FileInputStream(f);
			ObjectInputStream oInStream = new ObjectInputStream(fInStream);
			auth = (IAuthServer) oInStream.readObject();
			oInStream.close();
		}
		return auth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer gotReference() throws RemoteException {
		int leaseValue;
		String leaseProperty = System.getProperty("java.rmi.dgc.leaseValue");
		Integer clientID;
		if (leaseProperty == null)
			leaseValue = 600000;
		else
			leaseValue = Integer.parseInt(leaseProperty);
		synchronized (maxClientID) {
			clientID = maxClientID++;
		}
		DGCTask task = new DGCTask();
		dgcTimer.schedule(task, leaseValue);
		dgcTasks.put(clientID, task);
		synchronized (refCount) {
			refCount++;
		}
		System.out.println("Nuova referenza remota - ID=" + clientID + " [Totale: " + refCount + "]");
		return clientID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void releaseReference(Integer clientID) throws RemoteException {
		int count;
		DGCTask task = dgcTasks.remove(clientID);
		if (task != null)
			task.cancel();
		synchronized (refCount) {
			count = --refCount;
		}
		System.out.println("Referenza remota non più in uso - ID=" + clientID + " [Restanti: " + refCount + "]");
		if (count == 0)
			unreferenced();
	}
	
	/**
	 * Deregistra il server dai servizi di naming e lo deesporta,
	 * rendendolo disponibile per la garbage collection locale.
	 */
	private void unreferenced() {
		try {
			dgcTimer.cancel();
			System.out.println("Deregistrazione dai servizi di naming");
			RMIRegistry.unbind("BootstrapServer");
			COSNaming.unbind("BootstrapServer");
			System.out.println("Deesportazione del server");
			UnicastRemoteObject.unexportObject(this, false);
			PortableRemoteObject.unexportObject(this);
		} catch (NoSuchObjectException | NamingException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	/**
	 * Task che viene lanciato alla scadenza di un lease.
	 * @author Giacomo Annaloro
	 * @author Davide Mazzoni
	 *
	 */
	private class DGCTask extends TimerTask {

		/**
		 * Rimuove dalla lista dei client attivi quello corrispondente al
		 * lease scaduto, e decrementa il conteggio delle referenze.<br>
		 * Quando il conteggio va a 0, viene invocato {@link BootstrapServer#unreferenced()}.
		 * @see BootstrapServer#dgcTasks
		 * @see BootstrapServer#unreferenced()
		 */
		@Override
		public void run() {
			int count;
			Set<Integer> keys = BootstrapServer.this.dgcTasks.keySet();
			
			for (Integer i : keys) {
				DGCTask t = BootstrapServer.this.dgcTasks.get(i);
				if (this.equals(t)) {
					BootstrapServer.this.dgcTasks.remove(i);
					System.out.println("Referenza remota scaduta - ID=" + i);
					break;
				}
			}
			
			synchronized (refCount) {
				count = --refCount;
			}
			if (count == 0)
				BootstrapServer.this.unreferenced();
		}
		
	}
}
