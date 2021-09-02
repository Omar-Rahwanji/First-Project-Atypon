package server_components;
import cache_components.Cache;
import database_components.dao.DAOFactory;
import database_components.Entity;
import database_components.entities.NullEntity;
import operations_components.*;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Handler {
    private Cache[] cachedRecords;

    public ClientHandler(){}
    public ClientHandler(Socket clientSocket, Cache[] cachedRecords) {
        this.clientSocket = clientSocket;
        this.cachedRecords = cachedRecords;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void freeCachedQueries(){
        cachedRecords[0].wipe();
    }

    @Override
    public void run() {
        try{
            output.println("Welcome to the Customers Database System,");
            output.println("To be authorized to edit the db, please insert the admin key:");
            String key = input.readLine();
            var authorized = false;
            while(true){
                output.println("Choose the table: [0]: Customers, [1]: Accounts");
                int tableIndex;
                tableIndex=Integer.parseInt(input.readLine());
                OperationsUtilities.chooseTable(tableIndex);
                var entitiesDAO = DAOFactory.getInstanceByType(OperationsUtilities.entity);
                output.println("Choose one of the operations:");
                output.println("[1]: Read");
                if(key.equals("0000")){ //Admin
                    authorized=true;
                    output.println("[2]: Insert");
                    output.println("[3]: Update");
                    output.println("[4]: Delete");
                }
                output.println("[0]: Exit");
                var operations = new Operations();
                String operationChoice=input.readLine();
                switch (operationChoice) {
                    case "1": //Read
                        output.println("enter the delimiter:%n");
                        String delimiter=input.readLine();
                        Entity selectedRecord = operations.readRecord(tableIndex, delimiter, this.cachedRecords);
                        var records = new StringBuilder();
                        if(tableIndex==0)
                            records.append("ID, Name, Country, Phone, Email:%n-------------------------------------%n");
                        else
                            records.append("Account ID, Customer ID, Username:%n-------------------------------------%n");
                        if(!selectedRecord.equals(NullEntity.getInstance()))
                            records.append(entitiesDAO.recordToString(selectedRecord)).append("%n");
                        output.println((records.append("-------------------------------------%n%n")));
                        break;
                    case "2": //Insert
                        if(!authorized)
                            break;
                        if(tableIndex==0)
                            output.println("insert csv respectively (id should be unique):%nid,name,country,phone,email%n");
                        else
                            output.println("insert csv respectively (id should be unique):%nid,customerId,username%n");
                        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String newRecord = input.readLine();
                        boolean doneInsert = operations.insertRecord(tableIndex, newRecord, this.cachedRecords);
                        if (doneInsert){
                            output.println("Done inserting :)%n");
                        }
                        else
                            output.println("Inserting failed!%n");
                        break;
                    case "3": //Update
                        if(!authorized)
                            break;
                        output.println("enter the delimiter, then the new value for a column:%ncolumn><=value,col=new_value%n");
                        delimiter=input.readLine();
                        if(operations.updateRecord(tableIndex, delimiter, this.cachedRecords))
                            output.println("Done updating :)%n");
                        else
                            output.println("Updating failed!%n");
                        break;
                    case "4": //Delete
                        if(!authorized)
                            break;
                        output.println("enter the delimiter:%n");
                        delimiter=input.readLine();
                        if(operations.deleteRecord(tableIndex, delimiter, this.cachedRecords))
                            output.println("Done deleting :)%n");
                        else
                            output.println("Error while deleting!%n");
                        break;
                    default:
                        return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

