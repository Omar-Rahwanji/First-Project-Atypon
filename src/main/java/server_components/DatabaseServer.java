package server_components;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class DatabaseServer extends Server {
    private static DatabaseServer databaseServer;

    private DatabaseServer(int port, int maxLoad) {
        setPort(port);
        clients = new ArrayList<>();
        pool = Executors.newFixedThreadPool(maxLoad);
    }

    public static DatabaseServer getDatabaseServer(int port, int maxLoad) {
        if (databaseServer == null)
            databaseServer = new DatabaseServer(port, maxLoad);
        return databaseServer;
    }

    @Override
    public void setPort(int port) {
        if (port > 2000 && port < 65000)
            this.port = port;
    }
}
