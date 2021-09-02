package server_components;

public class ServerOperator {
    public static void main(String[] args) {
        Server server = DatabaseServer.getDatabaseServer(9876, 1000);
        server.turnOn();
    }
}
