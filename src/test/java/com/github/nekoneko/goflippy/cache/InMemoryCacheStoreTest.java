package com.github.nekoneko.goflippy.cache;

import com.github.nekoneko.goflippy.gson.Feature;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryCacheStoreTest {
    @Test
    public void FeatureCanBePut() {
        InMemoryCacheStore store = new InMemoryCacheStore(30);
        Feature feature = new Feature();
        feature.setKey("TEST");
        feature.setEnabled(true);
        store.put("cache-key", feature);

        assertEquals(store.get("cache-key"), feature);
    }

    @Test
    public void ExpiredFeature() throws Exception {
        InMemoryCacheStore store = new InMemoryCacheStore(1);
        Feature feature = new Feature();
        feature.setKey("TEST");
        feature.setEnabled(true);
        store.put("cache-key", feature);
        Thread.sleep(2000);

        assertNull(store.get("cache-key"));
    }

    // TODO: Add parallel test
}
