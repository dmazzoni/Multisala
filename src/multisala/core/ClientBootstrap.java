package multisala.core;

import java.io.IOException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * Codice di bootstrap del client minimale.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public class ClientBootstrap {

	/**
	 * Contatta il server di bootstrap per ottenere un'istanza dell'applicazione
	 * lato client. <br>Il server è contattato utilizzando host e protocollo
	 * specificati da linea di comando.
	 * @param args l'IP del server di bootstrap e il protocollo per la comunicazione
	 */
	public static void main(String[] args) {
		InitialContext cxt;
		Properties pr = new Properties();
		if(args.length != 1)
			throw new IllegalArgumentException("Utilizzo: multisala.core.ClientBootstrap <protocollo>");
		if(!args[0].equalsIgnoreCase("jrmp") && !args[0].equalsIgnoreCase("iiop"))
			throw new IllegalArgumentException("Protocollo: jrmp o iiop");
		if (System.getSecurityManager() == null) 
			System.setSecurityManager(new SecurityManager());
		String certPath = System.getProperty("multisala.certLocation");
		String serverAddress = System.getProperty("multisala.serverAddress");
		System.setProperty("javax.net.ssl.keyStore", certPath + "clientKeys");
		System.setProperty("javax.net.ssl.keyStorePassword", "multisala");
		System.setProperty("javax.net.ssl.trustStore", certPath + "clientTrust");
		System.setProperty("javax.net.ssl.trustStorePassword", "multisala");
		
		try {
			if (args[0].equalsIgnoreCase("jrmp")) {
	            pr.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
	            pr.put(Context.PROVIDER_URL, "rmi://" + serverAddress + ":1098");
	            cxt = new InitialContext(pr);
			} else {
				pr.put("java.naming.factory.initial", "com.sun.jndi.cosnaming.CNCtxFactory");
		        pr.put("java.naming.provider.url", "iiop://" + serverAddress + ":2098");
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
