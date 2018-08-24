package com.github.nekoneko.goflippy.config;

public class GoFlippyConfig {
    private String uri;
    private String apiKey;
    private long cacheLifeTimeSeconds;
    private long connectionTimeout;
    private long readTimeout;
    private long writeTimeout;

    protected GoFlippyConfig(GoFlippyConfigBuilder builder) {
        this.uri = builder.uri;
        this.apiKey = builder.apiKey;
        this.cacheLifeTimeSeconds = builder.cacheLifeTimeSeconds;
        this.connectionTimeout = builder.connectionTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
    }

    public String getUri() {
        return uri;
    }

    public String apiKey() {
        return apiKey;
    }

    public long getCacheLifeTimeSeconds() {
        return cacheLifeTimeSeconds;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }
}
