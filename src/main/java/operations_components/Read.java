package operations_components;

import cache_components.Cache;
import database_components.Entity;

public interface Read {
    Entity readRecord(int tableIndex, String delimiter, Cache[] cachedRecords);
}
