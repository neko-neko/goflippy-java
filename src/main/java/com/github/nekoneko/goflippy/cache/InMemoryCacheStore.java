package com.github.nekoneko.goflippy.cache;

import com.github.nekoneko.goflippy.gson.Feature;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A thread safe in memory cache store
 */
public class InMemoryCacheStore extends CacheStore {
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<String, FeatureWithExpire> map = new HashMap<>();

    class FeatureWithExpire {
        private Feature feature;
        private long expire;

        public FeatureWithExpire(Feature feature, long expire) {
            this.feature = feature;
            this.expire = expire;
        }
    }

    /**
     * Constructor
     *
     * @param cacheLifeTimeSeconds cache lifetime
     */
    public InMemoryCacheStore(long cacheLifeTimeSeconds) {
        super(cacheLifeTimeSeconds);
    }

    @Override
    public void put(String key, Feature feature) {
        lock.writeLock().lock();
        map.put(key, new FeatureWithExpire(feature, Instant.now().plusSeconds(cacheLifeTimeSeconds).getEpochSecond()));
        lock.writeLock().unlock();
    }

    @Override
    public Feature get(String key) {
        FeatureWithExpire feature;

        lock.readLock().lock();
        feature = map.get(key);
        lock.readLock().unlock();
        if (feature == null) {
            return null;
        }

        long now = Instant.now().getEpochSecond();
        if (now > feature.expire) {
            lock.writeLock().lock();
            map.remove(key);
            lock.writeLock().unlock();
            return null;
        }

        return feature.feature;
    }
}