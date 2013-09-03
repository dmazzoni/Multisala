package multisala.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.activation.ActivationGroupID;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import multisala.gui.GuestUI;

public class BootstrapServer implements IBootstrapServer, IGarbageCollection {
	
	private IGuest mobileAgent;
	
	private InitialContext RMIRegistry;
	private InitialContext COSNaming;
	
	private Integer refCount = 0;
	private Timer dgcTimer;
	private ConcurrentMap<Integer, DGCTask> dgcTasks;
	private static Integer maxClientID = 0;

	public BootstrapServer(IAuthServer auth, InitialContext cxt1, InitialContext cxt2) throws RemoteException {
		mobileAgent = auth.login();
		System.out.println("Guest mobile agent: " + mobileAgent);
		RMIRegistry = cxt1;
		COSNaming = cxt2;
		dgcTimer = new Timer();
		dgcTasks = new ConcurrentHashMap<Integer, DGCTask>();
		UnicastRemoteObject.exportObject(this);
		PortableRemoteObject.exportObject(this);
		gotReference();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        try {
        	IBootstrapServer bootServer;
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

			bootServer = new BootstrapServer(auth, cxt1, cxt2);
			cxt1.rebind("BootstrapServer", bootServer);
			cxt2.rebind("BootstrapServer", bootServer);
		} catch (ClassNotFoundException | NamingException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MarshalledObject<Runnable> getClient() throws RemoteException {
		return wrap(new GuestUI(mobileAgent));
	}
	
	private MarshalledObject<Runnable> wrap(Runnable obj) {
		try {
			return new MarshalledObject<Runnable>(obj);
		} catch (IOException e) {
			return null;
		}
	}
	
	private static IAuthServer getAuthRef(InitialContext cxt) throws ClassNotFoundException, IOException {
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
		} catch (NamingException e) {
			FileInputStream fInStream = new FileInputStream(f);
			ObjectInputStream oInStream = new ObjectInputStream(fInStream);
			auth = (IAuthServer) oInStream.readObject();
			oInStream.close();
		}
		return auth;
	}

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

	private class DGCTask extends TimerTask {

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
