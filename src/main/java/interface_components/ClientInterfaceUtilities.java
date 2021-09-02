package interface_components;
import client_components.Client;

import java.util.Scanner;

public class ClientInterfaceUtilities {
    private Client client;

    ClientInterfaceUtilities(Client client){
        this.client=client;
    }

    public void showWelcomePage(){
        System.out.println(client.receiveResponse());
    }

    public void showTables(){System.out.println(client.receiveResponse());}

    public void showOperationsMenu(boolean isAuthorized){
        System.out.println(client.receiveResponse());
        System.out.println(client.receiveResponse()); //Read operation
        if(isAuthorized) //Admin
            for (var i = 0; i < 3; i++)
                System.out.println(client.receiveResponse());
        System.out.println(client.receiveResponse()); //Exit option
    }

    public String readOperation(){
        var scanner=new Scanner(System.in);
        return scanner.next();
    }

    public boolean isAuthorized(){
        System.out.println(client.receiveResponse());
        var scanner = new Scanner(System.in);
        String adminKey = scanner.next();
        client.sendRequest(adminKey);
        return adminKey.equals("0000");
    }

    public void doTheOperation(){
        var scanner = new Scanner(System.in);
        System.out.printf(client.receiveResponse());
        String delimiter = scanner.next();
        client.sendRequest(delimiter);
        String records = client.receiveResponse();
        System.out.printf(records);
    }
}
