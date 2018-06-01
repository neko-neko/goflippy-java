package com.github.nekoneko.goflippy.cache;

import com.github.nekoneko.goflippy.gson.Feature;

public abstract class CacheStore {
    /**
     * Cache lifetime seconds
     * 0 is disable
     */
    protected long cacheLifeTimeSeconds;

    /**
     * Constructor
     * @param cacheLifeTimeSeconds cache TTL
     */
    public CacheStore(long cacheLifeTimeSeconds) {
        this.cacheLifeTimeSeconds = cacheLifeTimeSeconds;
    }

    /**
     * Put a {@link com.github.nekoneko.goflippy.gson.Feature} object
     *
     * @param key cache key
     * @param feature cache object {@link com.github.nekoneko.goflippy.gson.Feature}
     */
    public abstract void put(String key, Feature feature);

    /**
     * Get a {@link com.github.nekoneko.goflippy.gson.Feature} object from cache store
     *
     * @param key cache key
     * @return feature instance
     */
    public abstract Feature get(String key);

    /**
     * Returns cache enabled?
     * @return true/cache enabled
     */
    public boolean cacheEnabled() {
        return this.cacheLifeTimeSeconds > 0;
    }
}
