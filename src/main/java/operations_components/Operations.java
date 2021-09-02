package operations_components;

import cache_components.Cache;
import cache_components.CacheRecord;
import database_components.dao.DAOFactory;
import database_components.Entity;
import database_components.EntityFactory;
import database_components.entities.NullEntity;

import java.io.*;

public class Operations implements Read, Insert, Update, Delete {
    private final EntityFactory entityFactory = EntityFactory.getEntityFactory();
    private static final String TABLES_PATH = "tables/";
    @Override
    public Entity readRecord(int tableIndex, String delimiter, Cache[] databaseCache) {
        return OperationsUtilities.searchRecords(tableIndex, delimiter, databaseCache);
    }

    @Override
    public boolean insertRecord(int tableIndex, String newRecord, Cache[] databaseCache) {
        OperationsUtilities.chooseTable(tableIndex);
        var entitiesDAO = DAOFactory.getInstanceByType(OperationsUtilities.entity);
        try (var writer = new BufferedWriter(new FileWriter(TABLES_PATH + OperationsUtilities.table, true))) {
            String[] customerAttributes = newRecord.split(",");
            var recordId = Integer.parseInt(customerAttributes[0]);
            Entity similarRecord = OperationsUtilities.searchRecords(tableIndex,"id=" + recordId, databaseCache);
            if (similarRecord.equals(NullEntity.getInstance())) {
                var row = entityFactory.getEntityByType(OperationsUtilities.entity);
                row.setAttributes(customerAttributes);
                row.setDatabaseEntity(row);
                writer.write(entitiesDAO.recordToString(row) + System.lineSeparator());
                writer.flush();
                databaseCache[tableIndex].put(recordId, new CacheRecord(row));
                return true;
            }
            return false;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRecord(int tableIndex, String updateStatement, Cache[] databaseCache) {
        OperationsUtilities.chooseTable(tableIndex);
        var entitiesDAO = DAOFactory.getInstanceByType(OperationsUtilities.entity);
        try {
            String[] commands = updateStatement.split(","); // column><=value,column=new_value
            Entity toBeUpdatedRecord = OperationsUtilities.searchRecords(tableIndex, commands[0], databaseCache);
            String[] recordDelimiter = commands[0].split("=");
            String[] newColumnValue = commands[1].split("=");
            var recordId = Integer.parseInt(recordDelimiter[1]);

            if (newColumnValue[0].equals("id")) // ID should remain unique
                return false;
            databaseCache[tableIndex].deleteByKey(recordId);
            deleteRecord(tableIndex, commands[0], databaseCache);
            OperationsUtilities.chooseTable(tableIndex);
            entitiesDAO.setAttributeValue(toBeUpdatedRecord, newColumnValue[0], newColumnValue[1]);
            insertRecord(tableIndex, entitiesDAO.recordToString(toBeUpdatedRecord), databaseCache);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRecord(int tableIndex, String delimiter, Cache[] databaseCache) {
        OperationsUtilities.chooseTable(tableIndex);
        var entitiesDAO = DAOFactory.getInstanceByType(OperationsUtilities.entity);
        try (var reader = new BufferedReader(new FileReader(TABLES_PATH + OperationsUtilities.table));
             var writer = new BufferedWriter(new FileWriter(TABLES_PATH + "temp-"+OperationsUtilities.table))) {
            Entity recordToDelete = OperationsUtilities.searchRecords(tableIndex, delimiter, databaseCache);
            String recordFromDatabase;
            writer.write("");
            while (((recordFromDatabase = reader.readLine()) != null)) {
                if (recordFromDatabase.equals(entitiesDAO.recordToString(recordToDelete)))
                    continue;
                writer.append(recordFromDatabase).append(System.lineSeparator());
                writer.flush();
            }
            String[] tokens=OperationsUtilities.getDelimiterTokens(delimiter);
            databaseCache[tableIndex].deleteByKey(Integer.parseInt(tokens[1]));
            try (var input = new BufferedReader(new FileReader( TABLES_PATH + "temp-"+OperationsUtilities.table));
                 var output = new BufferedWriter(new FileWriter(TABLES_PATH + OperationsUtilities.table))) {
                output.write("");
                while ((recordFromDatabase = input.readLine()) != null) {
                    output.append(recordFromDatabase).append(System.lineSeparator());
                    output.flush();
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}