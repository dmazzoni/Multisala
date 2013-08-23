package multisala.core;

import java.rmi.Remote;
import java.util.List;

public interface IAdminMS extends Remote {
	
	List<String> confirmUsers(List<String> users);

}
