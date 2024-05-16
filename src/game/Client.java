package game;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection {
	
	private String ip;
	private int port;

	public Client(String ip, int port, Consumer<Serializable> onReceiveCallback) {
		super(onReceiveCallback);
		this.ip = ip;
		this.port = port;
	}

	@Override
	public boolean isServer() {
		return false;
	}

	@Override
	public String getIP() {
		return ip;
	}

	@Override
	public int getPort() {
		return port;
	}

}
