package multisala.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interfaccia remota del mobile server dell'amministratore.
 * @author Giacomo Annaloro
 * @author Davide Mazzoni
 *
 */
public interface IAdminMS extends Remote {
	
	/**
	 * Invocato dal server centrale per notificare all'amministratore la presenza 
	 * di nuovi utenti che hanno richiesto la registrazione nel sistema. 
	 * Ne mostra l'elenco in una finestra di dialogo e permette di 
	 * selezionare quali approvare.
	 * @param users la lista degli utenti in attesa di conferma
	 * @return La lista degli utenti approvati.
	 */
	List<String> confirmUsers(List<String> users) throws RemoteException;
	
	/**
	 * Invocato dal server centrale per informare l'amministratore che
	 * sono stati venduti tutti i biglietti per un dato spettacolo.
	 * @param sh lo spettacolo di cui sono esauriti i posti disponibili
	 */
	void showSoldOut(Show sh) throws RemoteException;

}
