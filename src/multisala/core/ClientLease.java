package multisala.core;

import java.io.Serializable;

public class ClientLease implements Serializable {
	
	private long duration;
	private int clientID;
	private static Integer maxID;
	
	public ClientLease(long duration) {
		this.duration = duration;
		synchronized (maxID) {
			this.clientID = maxID++;
		}
	}

	public long getDuration() {
		return duration;
	}

	public int getClientID() {
		return clientID;
	}

}
