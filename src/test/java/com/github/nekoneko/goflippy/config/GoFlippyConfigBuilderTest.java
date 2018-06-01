package com.github.nekoneko.goflippy.config;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GoFlippyConfigBuilderTest {
    @Test
    public void uriCanBeSet() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().uri("TEST").build();
        assertEquals("TEST", config.getUri());
    }

    @Test
    public void apiKeyCanBeSet() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().apiKey("TEST").build();
        assertEquals("TEST", config.apiKey());
    }

    @Test
    public void cacheLifeTimeSecondsCanBeSet() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().cacheLifeTimeSeconds(10).build();
        assertEquals(10, config.getCacheLifeTimeSeconds());
    }

    @Test
    public void connectionTimeOutCanBeSet() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().connectionTimeout(10).build();
        assertEquals(10, config.getConnectionTimeout());
    }

    @Test
    public void readTimeOutCanBeSet() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().readTimeout(10).build();
        assertEquals(10, config.getReadTimeout());
    }

    @Test
    public void writeTimeOutCanBeSet() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().writeTimeout(10).build();
        assertEquals(10, config.getWriteTimeout());
    }

    @Test
    public void ReturnDefaultValueWhenCacheLifeTimeSecondsIsNotSetTheValue() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().build();
        assertEquals(GoFlippyConfigBuilder.DEFAULT_CACHE_LIFE_TIME_SECONDS, config.getCacheLifeTimeSeconds());
    }

    @Test
    public void ReturnDefaultValueWhenConnectionTimeOutIsNotSetTheValue() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().build();
        assertEquals(GoFlippyConfigBuilder.DEFAULT_CONNECTION_TIME_OUT, config.getConnectionTimeout());
    }

    @Test
    public void ReturnDefaultValueWhenReadTimeOutIsNotSetTheValue() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().build();
        assertEquals(GoFlippyConfigBuilder.DEFAULT_READ_TIME_OUT, config.getReadTimeout());
    }

    @Test
    public void ReturnDefaultValueWhenWriteTimeOutIsNotSetTheValue() {
        GoFlippyConfig config = new GoFlippyConfigBuilder().build();
        assertEquals(GoFlippyConfigBuilder.DEFAULT_WRITE_TIME_OUT, config.getWriteTimeout());
    }
}
