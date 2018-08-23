package com.github.nekoneko.goflippy.cache;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread safe in memory cache store
 */
public class InMemoryCacheStore extends CacheStore {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<String, CacheEntry> map = new HashMap<>();

    /**
     * Constructor
     *
     * @param cacheLifeTimeSeconds cache lifetime
     */
    public InMemoryCacheStore(long cacheLifeTimeSeconds) {
        super(cacheLifeTimeSeconds);
    }

    @Override
    public void put(String key, Object obj) {
        lock.writeLock().lock();
        map.put(key, new CacheEntry(obj, Instant.now().plusSeconds(cacheLifeTimeSeconds).getEpochSecond()));
        lock.writeLock().unlock();
    }

    @Override
    public Object get(String key) {
        CacheEntry ent;

        lock.readLock().lock();
        ent = map.get(key);
        lock.readLock().unlock();
        if (ent == null) {
            return null;
        }

        long now = Instant.now().getEpochSecond();
        if (now > ent.expire) {
            lock.writeLock().lock();
            map.remove(key);
            lock.writeLock().unlock();
            return null;
        }

        return ent.obj;
    }

    class CacheEntry {
        private Object obj;
        private long expire;

        public CacheEntry(Object obj, long expire) {
            this.obj = obj;
            this.expire = expire;
        }
    }
}