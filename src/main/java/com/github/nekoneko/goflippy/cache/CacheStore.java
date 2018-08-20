package com.github.nekoneko.goflippy.cache;

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
     * Put a object
     *
     * @param key cache key
     * @param obj cache object {@link com.github.nekoneko.goflippy.gson.Feature}
     */
    public abstract void put(String key, Object obj);

    /**
     * Get a object from cache store
     *
     * @param key cache key
     * @return cache object
     */
    public abstract Object get(String key);

    /**
     * Returns cache enabled?
     * @return true/cache enabled
     */
    public boolean cacheEnabled() {
        return this.cacheLifeTimeSeconds > 0;
    }
}
