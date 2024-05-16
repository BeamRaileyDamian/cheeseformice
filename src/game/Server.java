package game;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkConnection{

	private int port;
	
	public Server(int port, Consumer<Serializable> onReceiveCallback) {
		super(onReceiveCallback);
		this.port = port;
	}

	@Override
	public boolean isServer() {
		return true;
	}

	@Override
	public String getIP() {
		return null;
	}

	@Override
	public int getPort() {
		return port;
	}

}
