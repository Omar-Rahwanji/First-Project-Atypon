package server_components;
import cache_components.Cache;
import cache_components.CacheFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class Server {
    protected Socket socket;
    protected int port;
    protected ServerSocket serverSocket;
    protected List<Handler> clients;
    protected ExecutorService pool;
    protected Cache[] cachedRecords;
    public abstract void setPort(int port);

    public void turnOn(){
        try (var localServerSocket = new ServerSocket(port)){
            var cacheFactory = CacheFactory.getCacheFactory();
            cachedRecords = new Cache[2];
            cachedRecords[0] = cacheFactory.getCacheByType("Database", 500); //For Table 1
            cachedRecords[1] = cacheFactory.getCacheByType("Database", 500); //For Table 2
            System.out.println("[SERVER] is live & listening on port [" + port + "]\n");
            this.serverSocket = localServerSocket;
            serverSocket.setReuseAddress(true);
            while (true) {
                listenForRequest();
                handleRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            turnOff();
        }
    }

    public void listenForRequest(){
        try {
            System.out.println("[SERVER] is waiting for client connection ...");
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRequest(){
        System.out.println("[SERVER] is handling a request!\n");
        Handler clientThread = new ClientHandler(socket, cachedRecords);
        clients.add(clientThread);
        pool.execute(clientThread);
    }

    public void turnOff(){
        try {
            socket.close();
            serverSocket.close();
            pool.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
