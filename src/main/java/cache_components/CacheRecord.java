package cache_components;

import database_components.Entity;

public class CacheRecord extends CacheItem {
    public CacheRecord(Entity row) {
        this.row=row;
    }
}

