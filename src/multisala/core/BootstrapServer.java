package multisala.core;

import java.rmi.NoSuchObjectException;
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
			
			IAuthServer auth = (IAuthServer) cxt1.lookup("AuthServer");
			bootServer = new BootstrapServer(auth, cxt1, cxt2);
			
			cxt1.rebind("BootstrapServer", bootServer);
			cxt2.rebind("BootstrapServer", bootServer);
		} catch (RemoteException | NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Runnable getClient() throws RemoteException {
		return new GuestUI(mobileAgent);
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
		if (count == 0)
			unreferenced();
	}
	
	private void unreferenced() {
		try {
			dgcTimer.cancel();
			RMIRegistry.unbind("BootstrapServer");
			COSNaming.unbind("BootstrapServer");
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
