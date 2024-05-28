package game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private List<ConnectionThread> connectionThreads = new ArrayList<>();
    private Consumer<Serializable> onReceiveCallback;

    public NetworkConnection(Consumer<Serializable> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
    }

    public void startServer(int port, Server server, String playerName) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ConnectionThread connectionThread = new ConnectionThread(clientSocket);
                    connectionThreads.add(connectionThread);
                    connectionThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startClient(String ipAddress, int port, String playerName) {
    	System.out.println("server's IP: " + ipAddress);
        new Thread(() -> {
            try {
                Socket socket = new Socket(ipAddress, port);
                ConnectionThread connectionThread = new ConnectionThread(socket);
                connectionThreads.add(connectionThread);
                connectionThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void send(Serializable data) throws IOException {
        for (ConnectionThread connectionThread : connectionThreads) {
            connectionThread.send(data);
        }
    }

    public void closeConnections() throws IOException {
        for (ConnectionThread connectionThread : connectionThreads) {
            connectionThread.close();
        }
    }

    public abstract boolean isServer();
    public abstract String getIP();
    public abstract int getPort();




    //  ****************************** UDP METHODS ******************************

//    public void sendUDP(Serializable data) throws IOException {
//        System.out.println("sendUDP being called");
//        byte[] dataBytes = serialize(data);
//
//        for (ConnectionThread connectionThread : connectionThreads) {
//            InetAddress ipAddress = connectionThread.getSocket().getInetAddress();
//            int port = connectionThread.getSocket().getPort();
//
//            DatagramPacket packet = new DatagramPacket(dataBytes, dataBytes.length, ipAddress, port);
//
//            try (DatagramSocket datagramSocket = new DatagramSocket()) {
//                datagramSocket.send(packet);
//            }
//        }
//    }
//
//    private byte[] serialize(Serializable obj) throws IOException {
//        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
//            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
//                o.writeObject(obj);
//            }
//            return b.toByteArray();
//        }
//    }

    //  ****************************** END OF UDP METHODS ******************************




    private class ConnectionThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;

        public ConnectionThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                    broadcast(data);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void send(Serializable data) throws IOException {
            out.writeObject(data);
//            out.reset();
        }

        public void close() throws IOException {
            socket.close();
        }

        private void broadcast(Serializable data) throws IOException {
            for (ConnectionThread connectionThread : connectionThreads) {
                if (connectionThread != this) {
                    connectionThread.send(data);
                }
            }
        }

        public Socket getSocket() {
            return this.socket;
        }

    }
}
