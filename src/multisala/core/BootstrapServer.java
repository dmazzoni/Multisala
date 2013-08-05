package multisala.core;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class BootstrapServer implements IBootstrapServer, IGarbageCollection {
	private IAuthServer authentication;
	
	private int refCount;
	private Timer dgcTimer;
	private Map<Integer, TimerTask> dgcTasks;

	public BootstrapServer(IAuthServer auth) throws RemoteException {
		authentication = auth;
		dgcTimer = new Timer();
		dgcTasks = new HashMap<Integer, TimerTask>();
		UnicastRemoteObject.exportObject(this);
		PortableRemoteObject.exportObject(this);
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
			IAuthServer auth = (IAuthServer) cxt1.lookup("AuthServer");
			bootServer = new BootstrapServer(auth);
			cxt1.rebind("BootstrapServer", bootServer);
			
			Properties prop2 = new Properties();
			prop2.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
			prop2.put("java.naming.provider.url", "iiop://localhost:2098");
			InitialContext cxt2 = new InitialContext(prop2);
			cxt2.rebind("BootstrapServer", bootServer);
		} catch (RemoteException | NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public GuestUI getClient() throws RemoteException {
		return new GuestUI(authentication.login());
	}

	@Override
	public ClientLease gotReference() throws RemoteException {
		int leaseValue = Integer.parseInt(System.getProperty("java.rmi.dgc.leaseValue"));
		ClientLease lease = new ClientLease(leaseValue);
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				
			}
		};
	}

	@Override
	public ClientLease refreshReference(ClientLease previous) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseReference(ClientLease previous) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
