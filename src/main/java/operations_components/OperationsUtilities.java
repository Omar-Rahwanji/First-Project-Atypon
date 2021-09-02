package operations_components;

import cache_components.Cache;

import cache_components.CacheItem;
import cache_components.CacheRecord;
import com.opencsv.CSVReaderBuilder;

import database_components.Entity;
import database_components.EntityFactory;
import database_components.entities.NullEntity;

import java.io.FileReader;

public class OperationsUtilities {
    private static final String TABLE1 = "customersTable.csv";
    private static final String TABLE2 = "accountsTable.csv";

    private static final String ENTITY1 = "customer";
    private static final String ENTITY2 = "account";
    public static String table;
    public static String entity;

    private static final EntityFactory entityFactory = EntityFactory.getEntityFactory();

    private OperationsUtilities(){}

    public static void chooseTable(int tableIndex){
        if(tableIndex == 0){
            table = TABLE1;
            entity = ENTITY1;
        }
        else{
            table = TABLE2;
            entity = ENTITY2;
        }
    }

    public static String[] getDelimiterTokens(String delimiter){ // [col,val,comparison]
        var tokens = new String[3];
        String[] delimiterTokens = delimiter.split("=");
        tokens[0]=delimiterTokens[0];
        tokens[1]=delimiterTokens[1];
        tokens[2]="=";
        return tokens;
    }

    public static Entity searchRecords(int tableIndex, String delimiter, Cache[] cachedRecords) {
        chooseTable(tableIndex);
        try (var fileReader = new FileReader("tables/" + table);
            var csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build()) {
            String[] nextRecord;
            String[] tokens = getDelimiterTokens(delimiter);
            String comparisonToken=tokens[2];
            var column = 0;
            if (comparisonToken.equals("=")) {
                Entity selectedRecord = cachedRecords[tableIndex].get(Integer.parseInt(tokens[1]));
                if (selectedRecord!=NullEntity.getInstance()){ //Cache hit
                    System.out.println("cache hit & id = "+tokens[1]);
                    return selectedRecord;
                }
                System.out.println("cache miss & id = "+tokens[1]);
                while ((nextRecord = csvReader.readNext()) != null) //Cache miss
                    if (nextRecord[column].equals(tokens[1])) { //Record Id
                        var row = entityFactory.getEntityByType(entity);
                        row.setAttributes(nextRecord);
                        CacheItem cachedRecord = new CacheRecord(row);
                        cachedRecords[tableIndex].put(Integer.parseInt(tokens[1]), cachedRecord);
                        return row;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NullEntity.getInstance();
    }
}
