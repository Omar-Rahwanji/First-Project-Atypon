package interface_components;

import client_components.Client;

public class ClientInterface {
    public static void main(String[] args){
        var client=new Client();
        var utility = new ClientInterfaceUtilities(client);
        client.connectToTheServer("localhost", 9876);
        utility.showWelcomePage();
        boolean isAuthorized = utility.isAuthorized();
        while (true) {
            utility.showTables();
            String tableIndex = utility.readOperation();
            client.sendRequest(tableIndex);
            utility.showOperationsMenu(isAuthorized);
            String operation = utility.readOperation();
            client.sendRequest(operation);
            if(operation.equals("0")){ //Exit option
                System.out.println("Good Bye :)");
                client.disconnect();
                return;
            }
            if(operation.equals("1") || ((operation.equals("2") || operation.equals("3") || operation.equals("4")) && isAuthorized))
                utility.doTheOperation();
            else
                System.out.printf("Invalid operation!%n");
        }
    }
}
