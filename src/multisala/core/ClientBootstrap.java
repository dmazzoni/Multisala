package multisala.core;

import java.io.IOException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class ClientBootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialContext cxt;
		Properties pr = new Properties();
		if(args.length != 2)
			throw new IllegalArgumentException("Utilizzo: multisala.core.ClientBootstrap <hostName> <protocol>");
		if(!args[1].equalsIgnoreCase("jrmp") && !args[1].equalsIgnoreCase("iiop"))
			throw new IllegalArgumentException("Protocollo: jrmp o iiop");
		if (System.getSecurityManager() == null) 
			System.setSecurityManager(new SecurityManager());
		
		try {
			if (args[1].equalsIgnoreCase("jrmp")) {
	            pr.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
	            pr.put(Context.PROVIDER_URL, "rmi://" + args[0] + ":1098");
	            cxt = new InitialContext(pr);
			} else {
				pr.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		        pr.put("java.naming.provider.url", "iiop://" + args[0] + ":2098");
		        cxt = new InitialContext(pr);
			}

            Object ref = cxt.lookup("BootstrapServer");
			IBootstrapServer bootServer = (IBootstrapServer) PortableRemoteObject.narrow(ref, IBootstrapServer.class);
			IGarbageCollection bootServerDGC = (IGarbageCollection) PortableRemoteObject.narrow(ref, IGarbageCollection.class);
			Integer myID = bootServerDGC.gotReference();
			System.out.println("Lancio finestra GuestUI...");
			Runnable client = bootServer.getClient().get();
			client.run();
			System.out.println("Completato");
			bootServerDGC.releaseReference(myID);
		} catch (ClassNotFoundException | IOException | NamingException e) {
			e.printStackTrace();
		} 
	}

}
